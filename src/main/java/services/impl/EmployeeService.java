package services.impl;

import mappers.EmployeeMapper;
import models.DTO.EmployeeDto;
import models.entities.Employee;
import persistence.manager.DatabaseSingleton;
import persistence.repositories.helpers.EmployeeProjection;
import persistence.repositories.impl.EmployeeRepository;
import services.BaseService;

import java.util.List;
import java.util.Set;

public class EmployeeService extends BaseService<Employee, EmployeeDto,Long>{
    protected EmployeeService() {
        super(EmployeeRepository.getInstance(), EmployeeMapper.INSTANCE);
    }

    private static class SingletonHelper {
        private static final EmployeeService INSTANCE = new EmployeeService();
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

}
