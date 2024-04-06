package services.impl;

import mappers.BaseMapper;
import mappers.EmployeeMapper;
import models.DTO.EmployeeDto;
import models.entities.Employee;
import persistence.repositories.GenericRepository;
import persistence.repositories.impl.EmployeeRepository;
import services.BaseService;

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



}
