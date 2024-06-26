package services.impl;

import mappers.DepartmentMapper;
import mappers.DtoMapper;
import mappers.EmployeeMapper;
import mappers.ProjectionMapper;
import models.DTO.DepartmentDto;
import models.DTO.EmployeeDto;
import models.entities.Department;
import models.entities.Employee;
import persistence.manager.DatabaseSingleton;
import persistence.repositories.helpers.projections.EmployeeProjection;
import persistence.repositories.impl.DepartmentRepository;
import services.BaseService;

import java.util.List;

public class DepartmentService extends BaseService<Department, DepartmentDto, Long> {
    protected DepartmentService() {
        super(DepartmentRepository.getInstance(), DepartmentMapper.INSTANCE);
    }

    public static DepartmentService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public boolean updateManager(Long departmentId, Long managerId) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            Department department = repository.read(departmentId, entityManager).orElse(null);
            Employee manager = entityManager.find(Employee.class, managerId);
            if (department==null){
                return false;
            }
            department.setManager(manager);
            return repository.update(department, entityManager);
        });
    }

    public DepartmentDto findByName(String name) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            Department department = DepartmentRepository.getInstance().findByName(name, entityManager);
            return DepartmentMapper.INSTANCE.toDTO(department);
        });
    }

    @Override
    public boolean delete(Long aLong) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            Department department = repository.read(aLong, entityManager).orElse(null);
            if (department!=null) {
                department.removeManager();
                department.setActive(false);
            }
            repository.update(department, entityManager);
            return repository.update(department, entityManager);
        });
    }

    public List<EmployeeProjection> getEmployees(Long id, int offset, int limit) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            List<Employee> employees = DepartmentRepository.getInstance().getEmployees(id, offset, limit, entityManager);
            return ProjectionMapper.INSTANCE.toProjectionList(employees);
        });
    }

    public Long countEmployees(Long id) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> DepartmentRepository.getInstance().countEmployees(id, entityManager));
    }

    private static class SingletonHelper {
        public static final DepartmentService INSTANCE = new DepartmentService();
    }
}
