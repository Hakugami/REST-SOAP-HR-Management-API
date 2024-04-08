package services.impl;

import mappers.EmployeeMapper;
import models.DTO.EmployeeDto;
import models.entities.Employee;
import models.enums.JobTitle;
import persistence.manager.DatabaseSingleton;
import persistence.repositories.impl.EmployeeRepository;
import persistence.repositories.impl.JobRepository;
import services.BaseService;
import utils.HashingUtil;
import utils.JWTUtil;

import java.util.Date;

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
        JobTitle jobTitle = JobTitle.ENTRY_LEVEL;
        int yearsOfExperience = employee.getYearsOfExperience();
        if (yearsOfExperience < 1) {
            jobTitle = JobTitle.ENTRY_LEVEL;
        } else if (yearsOfExperience < 2) {
            jobTitle = JobTitle.JUNIOR_DEVELOPER;
        } else if (yearsOfExperience < 3) {
            jobTitle = JobTitle.DEVELOPER;
        } else if (yearsOfExperience < 5) {
            jobTitle = JobTitle.SENIOR_DEVELOPER;
        }
        return jobTitle;
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

            // Determine JobTitle based on years of experience
            JobTitle jobTitle = getJobTitle(employee);

            // Retrieve Job entity and set to Employee
            JobRepository.getInstance().getJobByTitle(jobTitle, entityManager).ifPresent(employee::setJob);

            EmployeeRepository.getInstance().create(employee, entityManager);
            return EmployeeMapper.INSTANCE.toDTO(employee);
        });
    }

    private static class SingletonHelper {
        private static final AuthenticationService INSTANCE = new AuthenticationService();
    }

}
