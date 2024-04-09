package services.impl;

import lombok.extern.slf4j.Slf4j;
import mappers.DtoMapper;
import mappers.EmployeeMapper;
import models.DTO.EmployeeDto;
import models.entities.Attendance;
import models.entities.Employee;
import models.enums.AttendanceStatus;
import models.enums.Privilege;
import persistence.manager.DatabaseSingleton;
import persistence.repositories.helpers.projections.EmployeeProjection;
import persistence.repositories.impl.EmployeeRepository;
import services.BaseService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
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

    public boolean hire(Long id) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            Employee employee = EmployeeRepository.getInstance().read(id, entityManager);
            if (employee == null) {
                return false;
            }
            employee.setIsHired(true);
            employee.setHireDate(new Date());
            return EmployeeRepository.getInstance().update(employee, entityManager);
        });
    }

    public boolean assignManager(Long employeeId, Long managerId) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            log.info("Employee ID: {}", employeeId);
            Employee employee = EmployeeRepository.getInstance().read(employeeId, entityManager);
            if (employee == null) {
                log.info("Employee is null");
                return false;
            }
            log.info("Manager ID: {}", managerId);
            Employee manager = EmployeeRepository.getInstance().read(managerId, entityManager);
            if (manager == null) {
                log.info("Manager is null");
                return false;
            }
            log.info("Manager: {}", manager.getFirstName());
            employee.setManager(manager);
            return EmployeeRepository.getInstance().update(employee, entityManager);
        });
    }

    @Override
    public boolean update(EmployeeDto dto, Long aLong) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            Employee employee = EmployeeRepository.getInstance().read(aLong, entityManager);
            if (employee == null) {
                return false;
            }
            Employee updatedEmployee = EmployeeMapper.INSTANCE.updateEntity(dto, employee);
            return EmployeeRepository.getInstance().update(updatedEmployee, entityManager);
        });
    }

    public boolean delete(Long aLong, boolean isFired) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            Employee employee = EmployeeRepository.getInstance().read(aLong, entityManager);
            if (employee == null) {
                return false;
            }
            employee.setManager(null);
            employee.setDepartment(null);
            employee.setPrivilege(Privilege.ALL);
            employee.setSalary(BigDecimal.ZERO);

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

    public EmployeeProjection getManager(Long id) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            Employee employee = EmployeeRepository.getInstance().read(id, entityManager);
            if (employee == null) {
                return null;
            }
            Employee manager = employee.getManager();
            if (manager == null) {
                return null;
            }
            return DtoMapper.INSTANCE.employeeDtoToEmployee(EmployeeMapper.INSTANCE.toDTO(manager));
        });


    }

    private static class SingletonHelper {
        private static final EmployeeService INSTANCE = new EmployeeService();
    }
}
