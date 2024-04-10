package controllers.rest.resources.project;

import controllers.rest.annotations.Secured;
import controllers.rest.beans.PaginationBean;
import controllers.rest.helpers.utils.RestUtil;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import lombok.extern.slf4j.Slf4j;
import models.DTO.ProjectDto;
import models.enums.Privilege;
import services.impl.ProjectService;

import java.net.URI;
import java.util.List;

@Path("projects")
@Slf4j
@Secured(Privilege.HR)
public class ProjectController {
    @Context
    private UriInfo uriInfo;


    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getProjects(@BeanParam PaginationBean paginationBean, @QueryParam("type") String type) {
        log.info("Getting all projects...");
        List<ProjectDto> projects = ProjectService.getInstance().readAll(paginationBean.getOffset(), paginationBean.getLimit());
        ProjectResponseWrapper projectResponseWrapper = new ProjectResponseWrapper();
        projects.forEach(projectDto -> {
            ProjectResponse projectResponse = new ProjectResponse();
            projectResponse.setProjectDto(projectDto);
            projectResponse.addLink(RestUtil.createSelfLink(uriInfo, projectDto.getId(), ProjectController.class));
            projectResponseWrapper.getProjects().add(projectResponse);
        });
        for (Link link : RestUtil.createPaginatedResourceLink(uriInfo, paginationBean, ProjectService.getInstance().count())) {
            projectResponseWrapper.addLink(link);
        }
        return buildResponse(projectResponseWrapper, type);
    }

    private Response buildResponse(ProjectResponseWrapper projectResponseWrapper, String type) {
        if (type != null && type.equals("xml")) {
            return Response.ok(projectResponseWrapper).type(MediaType.APPLICATION_XML).build();
        } else {
            return Response.ok(projectResponseWrapper).type(MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getProject(@PathParam("id") Long id, @QueryParam("type") String type) {
        log.info("Getting project with id: {}", id);
        ProjectDto projectDto = ProjectService.getInstance().read(id);
        if (projectDto == null) {
            log.error("Project with id {} not found", id);
            return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
        }
        ProjectResponse projectResponse = new ProjectResponse();
        projectResponse.setProjectDto(projectDto);
        projectResponse.addLink(RestUtil.createSelfLink(uriInfo, projectDto.getId(), ProjectController.class));
        return buildResponse(projectResponse, type);
    }

    private Response buildResponse(ProjectResponse projectResponse, String type) {
        if (type != null && type.equals("xml")) {
            return Response.ok(projectResponse).type(MediaType.APPLICATION_XML).build();
        } else {
            return Response.ok(projectResponse).type(MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createProject(ProjectDto projectDto) {
        log.info("Creating project...");
        ProjectDto createdProject = ProjectService.getInstance().save(projectDto);
        if (createdProject == null) {
            log.error("Project not created");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Project not created").build();
        }
        log.info("Project created with id: {}", createdProject.getId());
        ProjectResponse projectResponse = new ProjectResponse();
        projectResponse.setProjectDto(createdProject);
        URI createdAtUriForPost = RestUtil.getCreatedAtUriForPost(uriInfo, createdProject.getId());
        Link selfLink = Link.fromUri(createdAtUriForPost).rel("self").build();
        projectResponse.addLink(selfLink);

        GenericEntity<ProjectResponse> entity = new GenericEntity<>(projectResponse) {
        };
        return Response.created(createdAtUriForPost).entity(entity).build();
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateProject(@PathParam("id") Long id, @Valid ProjectDto projectDto) {
        log.info("Updating project with id: {}", id);
        boolean updatedProject = ProjectService.getInstance().update(projectDto, id);
        if (!updatedProject) {
            log.error("Failed to update project");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to update project").build();
        } else {
            log.info("Project updated successfully");
            return getProject(id, projectDto);
        }
    }

    private Response getProject(Long id, ProjectDto projectDto) {
        ProjectResponse projectResponse = new ProjectResponse();
        projectResponse.setProjectDto(projectDto);
        projectResponse.addLink(RestUtil.createSelfLink(uriInfo, id, ProjectController.class));
        return Response.ok(projectResponse).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteProject(@PathParam("id") Long id) {
        log.info("Deleting project with id: {}", id);
        boolean deletedProject = ProjectService.getInstance().delete(id);
        if (!deletedProject) {
            log.error("Failed to delete project");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to delete project").build();
        } else {
            log.info("Project deleted successfully");
            return Response.ok().build();
        }
    }

    @PATCH
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response patchProject(@PathParam("id") Long id, ProjectDto projectDto) {
        log.info("Patching project with id: {}", id);
        boolean patchedProject = ProjectService.getInstance().update(projectDto, id);
        if (!patchedProject) {
            log.error("Failed to patch project");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to patch project").build();
        } else {
            log.info("Project patched successfully");
            return getProject(id, projectDto);
        }
    }


    @PATCH
    @Path("{id}/employees/{employeeId}")
    public Response assignEmployeeToProject(@PathParam("id") Long projectId, @PathParam("employeeId") Long employeeId) {
        return getResponse(projectId, employeeId);
    }

    @PUT
    @Path("{id}/employees/{employeeId}")
    public Response assignEmployeeToProjectPut(@PathParam("id") Long projectId, @PathParam("employeeId") Long employeeId) {
        return getResponse(projectId, employeeId);
    }

    private Response getResponse( Long projectId,  Long employeeId) {
        log.info("Assigning employee with id: {} to project with id: {}", employeeId, projectId);
        boolean assignedEmployee = ProjectService.getInstance().assignEmployeeToProject(projectId, employeeId);
        if (!assignedEmployee) {
            log.error("Failed to assign employee to project");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to assign employee to project").build();
        } else {
            log.info("Employee assigned to project successfully");
            return Response.ok("Employee assigned to project successfully").build();
        }
    }

}
