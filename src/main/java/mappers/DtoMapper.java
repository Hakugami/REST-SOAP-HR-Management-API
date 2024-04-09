package mappers;


import models.DTO.EmployeeDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import persistence.repositories.helpers.projections.EmployeeProjection;

@Mapper
public interface DtoMapper {

    DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);

    EmployeeDto employeeToEmployeeDto(EmployeeProjection employee);

    EmployeeProjection employeeDtoToEmployee(EmployeeDto employeeDto);
}
