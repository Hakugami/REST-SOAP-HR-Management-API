package services.impl;

import mappers.EmployeeMapper;
import models.DTO.EmployeeDto;
import models.entities.Attendance;
import models.entities.Employee;
import models.enums.AttendanceStatus;
import persistence.manager.DatabaseSingleton;
import persistence.repositories.helpers.EmployeeProjection;
import persistence.repositories.impl.EmployeeRepository;
import services.BaseService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class EmployeeService extends BaseService<Employee, EmployeeDto, Long> {
    protected EmployeeService() {
        super(EmployeeRepository.getInstance(), EmployeeMapper.INSTANCE);
    }

    public static EmployeeService getInstance() {
        return EmployeeService.SingletonHelper.INSTANCE;
    }




    public EmployeeProjection employeePartialResponse(Long id, Set<String> fields) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager ->
                EmployeeRepository.getInstance().employeePartialResponse(id, entityManager, fields).orElse(null));
    }

    public List<EmployeeProjection> getAllEmployeesPartialResponse(Set<String> fields, int page, int pageSize) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager ->
                EmployeeRepository.getInstance().getAllEmployeesPartialResponse(entityManager, fields, page, pageSize));
    }


    public boolean delete(Long aLong, boolean isFired) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            Employee employee = EmployeeRepository.getInstance().read(aLong, entityManager);
            if (employee == null) {
                return false;
            }
            employee.setIsHired(false);
            if (isFired) {
                employee.setFireDate(new Date());
            }
            return EmployeeRepository.getInstance().update(employee, entityManager);
        });
    }

    public boolean attendance(String email, AttendanceStatus status) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            Employee employee = EmployeeRepository.getInstance().readByEmail(email, entityManager).orElse(null);
            if (employee == null) {
                return false;
            }
            Attendance attendance = new Attendance();
            attendance.setEmployee(employee);
            attendance.setAttendanceTime(new Date());
            attendance.setStatus(status);
            employee.getAttendances().add(attendance);

            if (status == AttendanceStatus.ABSENT) {
                Double deduction = employee.getDeduction();
                BigDecimal updatedDeduction = BigDecimal.valueOf(deduction + 0.02);
                BigDecimal newDeduction = updatedDeduction.multiply(employee.getSalary());
                employee.setDeduction(newDeduction.doubleValue());
            } else if (status == AttendanceStatus.LATE) {
                Double deduction = employee.getDeduction();
                BigDecimal updatedDeduction = BigDecimal.valueOf(deduction + 0.01);
                BigDecimal newDeduction = updatedDeduction.multiply(employee.getSalary());
                employee.setDeduction(newDeduction.doubleValue());
            }
            return EmployeeRepository.getInstance().update(employee, entityManager);
        });
    }

    private static class SingletonHelper {
        private static final EmployeeService INSTANCE = new EmployeeService();
    }
}
