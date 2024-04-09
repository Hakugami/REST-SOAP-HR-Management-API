package persistence.repositories.impl;

import models.entities.Department;
import persistence.repositories.GenericRepository;

public class DepartmentRepository extends GenericRepository<Department,Long> {
    protected DepartmentRepository() {
        super(Department.class);
    }


    public static DepartmentRepository getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public static class SingletonHelper {
        public static final DepartmentRepository INSTANCE = new DepartmentRepository();
    }

}
