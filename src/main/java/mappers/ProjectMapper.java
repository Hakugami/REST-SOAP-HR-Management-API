package mappers;

import models.DTO.ProjectDto;
import models.entities.Project;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectMapper extends BaseMapper<Project, ProjectDto>{
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);
}
