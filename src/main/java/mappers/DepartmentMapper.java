package mappers;

import models.DTO.DepartmentDto;
import models.entities.Department;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department,DepartmentDto>{
    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);
}
