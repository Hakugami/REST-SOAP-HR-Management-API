package mappers;

import models.DTO.EmployeeDto;
import models.entities.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface EmployeeMapper extends BaseMapper<Employee, EmployeeDto>{
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);
}
