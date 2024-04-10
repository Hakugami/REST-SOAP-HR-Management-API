package services.impl;

import lombok.extern.slf4j.Slf4j;
import mappers.EmployeeMapper;
import models.DTO.EmployeeDto;
import models.entities.Department;
import models.entities.Employee;
import models.enums.JobTitle;
import persistence.manager.DatabaseSingleton;
import persistence.repositories.impl.DepartmentRepository;
import persistence.repositories.impl.EmployeeRepository;
import persistence.repositories.impl.JobRepository;
import services.BaseService;
import utils.HashingUtil;
import utils.JWTUtil;

import java.util.Date;

@Slf4j
public class AuthenticationService extends BaseService<Employee, EmployeeDto, Long> {
    private final JWTUtil jwtUtil;

    protected AuthenticationService() {
        super(EmployeeRepository.getInstance(), EmployeeMapper.INSTANCE);
        this.jwtUtil = new JWTUtil();
    }

    public static AuthenticationService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    private static JobTitle getJobTitle(Employee employee) {
        int yearsOfExperience = employee.getYearsOfExperience();
        log.info("Years of experience: {}", yearsOfExperience);

        if (yearsOfExperience < 1) {
            return JobTitle.ENTRY_LEVEL;
        } else if (yearsOfExperience < 2) {
            return JobTitle.JUNIOR_DEVELOPER;
        } else if (yearsOfExperience < 3) {
            return JobTitle.DEVELOPER;
        } else if (yearsOfExperience < 5) {
            return JobTitle.SENIOR_DEVELOPER;
        } else if (yearsOfExperience < 10) {
            return JobTitle.MANAGER;
        } else {
            return JobTitle.CEO;
        }
    }


    public String login(String username, String password) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            Employee employee = EmployeeRepository.getInstance().readByUsername(username, entityManager).orElse(null);
            if (employee == null) {
                return null;
            }
            String hashedPassword = HashingUtil.hashPasswordWithSalt(password, employee.getSalt());
            if (employee.getPassword().equals(hashedPassword)) {
                return jwtUtil.generateSignedJWT(employee);
            }
            return null;
        });
    }

    @Override
    public EmployeeDto save(EmployeeDto employeeDto) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            Employee employee = EmployeeMapper.INSTANCE.toEntity(employeeDto);
            String salt = HashingUtil.generateSalt();
            String password = HashingUtil.hashPasswordWithSalt(employee.getPassword(), salt);
            employee.setIsHired(true);
            employee.setHireDate(new Date());
            employee.setSalt(salt);
            employee.setPassword(password);
            if (employeeDto.getDepartmentId() != null) {
                Department department = DepartmentRepository.getInstance().read(employeeDto.getDepartmentId(), entityManager).orElse(null);
                employee.setDepartment(department);

                if (department!=null) {
                    employee.setManager(department.getManager());
                }
            } else if (employeeDto.getDepartmentName() != null) {
                Department department = DepartmentRepository.getInstance().findByName(employeeDto.getDepartmentName(), entityManager);
                employee.setDepartment(department);
                employee.setDepartment(department);
                employee.setManager(department.getManager());
            }
            // Determine JobTitle based on years of experience
            JobTitle jobTitle = getJobTitle(employee);

            // Retrieve Job entity and set to Employee
            JobRepository.getInstance().getJobByTitle(jobTitle, entityManager).ifPresent(job -> {
                log.info("Job found: {}", job.getTitle());
                employee.setJob(job);
            });

            EmployeeRepository.getInstance().create(employee, entityManager);
            return EmployeeMapper.INSTANCE.toDTO(employee);
        });
    }

    private static class SingletonHelper {
        private static final AuthenticationService INSTANCE = new AuthenticationService();
    }

}
