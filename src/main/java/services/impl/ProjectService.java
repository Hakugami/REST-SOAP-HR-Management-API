package services.impl;

import mappers.ProjectMapper;
import models.DTO.ProjectDto;
import models.entities.Project;
import persistence.repositories.impl.ProjectRepository;
import services.BaseService;

public class ProjectService extends BaseService<Project, ProjectDto,Long> {
    protected ProjectService() {
        super(ProjectRepository.getInstance(), ProjectMapper.INSTANCE);
    }

    public static ProjectService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    private static class SingletonHelper {
        public static final ProjectService INSTANCE = new ProjectService();
    }
}
