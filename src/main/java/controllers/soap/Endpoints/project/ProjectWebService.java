package controllers.soap.Endpoints.project;


import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.ws.BindingType;
import lombok.extern.slf4j.Slf4j;
import models.DTO.ProjectDto;
import services.impl.ProjectService;


import java.util.List;

@WebService
@Slf4j
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
@BindingType(jakarta.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class ProjectWebService{

    @WebMethod
    @WebResult(name = "projects")
    public List<ProjectDto> getProjects(@WebParam(name = "limit") int limit, @WebParam(name = "offset") int offset) {
        log.info("Getting all projects...");
        return ProjectService.getInstance().readAll(offset, limit);
    }

    @WebMethod
    @WebResult(name = "project")
    public ProjectDto getProject(@WebParam(name = "id") Long id) {
        log.info("Getting project with id: {}...", id);
        return ProjectService.getInstance().read(id);
    }

    @WebMethod
    public void createProject(@WebParam(name = "project") ProjectDto projectDto) {
        log.info("Creating project...");
        ProjectService.getInstance().save(projectDto);
    }

    @WebMethod
    public void updateProject(@WebParam(name = "id") Long id, @WebParam(name = "project") ProjectDto projectDto) {
        log.info("Updating project with id: {}...", id);
        ProjectService.getInstance().update(projectDto, id);
    }

    @WebMethod
    public void deleteProject(@WebParam(name = "id") Long id) {
        log.info("Deleting project with id: {}...", id);
        ProjectService.getInstance().delete(id);
    }

    @WebMethod
    public void assignEmployeeToProject(@WebParam(name = "projectId") Long projectId, @WebParam(name = "employeeId") Long employeeId) {
        log.info("Assigning employee with id: {} to project with id: {}...", employeeId, projectId);
        ProjectService.getInstance().assignEmployeeToProject(projectId, employeeId);
    }
}
