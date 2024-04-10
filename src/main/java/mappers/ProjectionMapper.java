package mappers;

import models.entities.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import persistence.repositories.helpers.projections.EmployeeProjection;
import java.util.List;

@Mapper
public interface ProjectionMapper {
    ProjectionMapper INSTANCE = Mappers.getMapper(ProjectionMapper.class);

    EmployeeProjection toProjection(Employee employee);
    Employee fromProjection(EmployeeProjection projection);
    List<EmployeeProjection> toProjectionList(List<Employee> employees);
    List<Employee> fromProjectionList(List<EmployeeProjection> projections);
}
