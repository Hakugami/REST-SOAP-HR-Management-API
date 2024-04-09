package services.impl;

import mappers.DtoMapper;
import mappers.EmployeeMapper;
import models.DTO.EmployeeDto;
import models.entities.Employee;
import persistence.manager.DatabaseSingleton;
import persistence.repositories.helpers.projections.EmployeeProjection;
import persistence.repositories.impl.EmployeeRepository;
import services.BaseService;

import java.util.List;
import java.util.Set;

public class ManagerService extends BaseService<Employee, EmployeeDto, Long> {
    protected ManagerService() {
        super(EmployeeRepository.getInstance(), EmployeeMapper.INSTANCE);
    }

    public static ManagerService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public List<EmployeeProjection> getAllManagersPartialResponse(Set<String> fields, int page, int pageSize) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager ->
                EmployeeRepository.getInstance()
                        .getAllManagersPartialResponse(entityManager, fields, page, pageSize));
    }

    public EmployeeProjection getManagerPartialResponse(Long id, Set<String> fields) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager ->
                EmployeeRepository.getInstance().getManagerPartialResponse(id, entityManager, fields));
    }

    public EmployeeProjection saveAndReturnProjection(EmployeeDto dto) {
        return DtoMapper.INSTANCE.employeeDtoToEmployee(super.save(dto));
    }

    private static class SingletonHelper {
        public static final ManagerService INSTANCE = new ManagerService();
    }
}
