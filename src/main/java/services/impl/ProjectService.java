package services.impl;

import mappers.ProjectMapper;
import models.DTO.ProjectDto;
import models.entities.Employee;
import models.entities.Project;
import models.enums.ProjectStatus;
import persistence.manager.DatabaseSingleton;
import persistence.repositories.impl.ProjectRepository;
import services.BaseService;

public class ProjectService extends BaseService<Project, ProjectDto,Long> {
    protected ProjectService() {
        super(ProjectRepository.getInstance(), ProjectMapper.INSTANCE);
    }

    public static ProjectService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public boolean assignEmployeeToProject(Long projectId, Long employeeId) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            Project project = ProjectRepository.getInstance().read(projectId, entityManager).orElse(null);
            if (project == null) {
                return false;
            }
            Employee employee = entityManager.find(Employee.class, employeeId);
            if (employee == null) {
                return false;
            }
            project.addEmployee(employee);
            return ProjectRepository.getInstance().update(project, entityManager);
        });
    }


    @Override
    public boolean delete(Long aLong) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            Project project = ProjectRepository.getInstance().read(aLong, entityManager).orElse(null);
            if (project == null) {
                return false;
            }
            project.getEmployees().forEach(employee -> employee.setProject(null));
            project.clearEmployees();
            project.setStatus(ProjectStatus.CANCELLED);
            return ProjectRepository.getInstance().update(project, entityManager);
        });
    }

    private static class SingletonHelper {
        public static final ProjectService INSTANCE = new ProjectService();
    }
}
