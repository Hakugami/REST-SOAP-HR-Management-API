package persistence.repositories.impl;

import models.DTO.ProjectDto;
import models.entities.Project;
import persistence.repositories.GenericRepository;

public class ProjectRepository extends GenericRepository<Project, Long> {
    public ProjectRepository() {
        super(Project.class);
    }

    public static ProjectRepository getInstance() {
        return SingletonHelper.INSTANCE;
    }


    private static class SingletonHelper {
        public static final ProjectRepository INSTANCE = new ProjectRepository();
    }
}
