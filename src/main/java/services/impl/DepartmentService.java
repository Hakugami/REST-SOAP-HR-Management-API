package services.impl;

import mappers.DepartmentMapper;
import models.DTO.DepartmentDto;
import models.entities.Department;
import models.entities.Manager;
import persistence.manager.DatabaseSingleton;
import persistence.repositories.impl.DepartmentRepository;
import services.BaseService;

public class DepartmentService extends BaseService<Department, DepartmentDto, Long> {
    protected DepartmentService() {
        super(DepartmentRepository.getInstance(), DepartmentMapper.INSTANCE);
    }

    public static DepartmentService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public boolean updateManager(Long departmentId, Long managerId) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            Department department = repository.read(departmentId, entityManager);
            Manager manager = entityManager.find(Manager.class, managerId);
            department.setManager(manager);
            return repository.update(department, entityManager);
        });
    }

    @Override
    public boolean delete(Long aLong) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            Department department = repository.read(aLong, entityManager);
            department.setManager(null);
            department.setActive(false);
            repository.update(department, entityManager);
            return repository.delete(aLong, entityManager);
        });
    }


    private static class SingletonHelper {
        public static final DepartmentService INSTANCE = new DepartmentService();
    }
}
