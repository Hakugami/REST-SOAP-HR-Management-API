package controllers.rest.resources.department;

import controllers.rest.annotations.Secured;
import controllers.rest.beans.PaginationBean;
import controllers.rest.exceptions.custom.ResourceNotFoundException;
import controllers.rest.helpers.utils.RestUtil;
import controllers.rest.resources.employee.EmployeeController;
import controllers.rest.resources.employee.EmployeeResponse;
import controllers.rest.resources.employee.EmployeeResponseWrapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import lombok.extern.slf4j.Slf4j;
import models.DTO.DepartmentDto;
import models.enums.Privilege;
import persistence.repositories.helpers.projections.EmployeeProjection;
import services.impl.DepartmentService;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Slf4j
@Path("departments")
@Secured(Privilege.HR)
public class DepartmentController {
    @Context
    private UriInfo uriInfo;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDepartments(@BeanParam PaginationBean paginationBean, @QueryParam("type") String type) {
        log.info("Getting all departments...");
        try {
            List<DepartmentDto> departments = DepartmentService.getInstance().readAll(paginationBean.getOffset(), paginationBean.getLimit());
            DepartmentResponseWrapper departmentResponseWrapper = new DepartmentResponseWrapper();
            for (DepartmentDto department : departments) {
                DepartmentResponse departmentResponse = new DepartmentResponse();
                departmentResponse.setDepartmentDto(department);
                departmentResponse.addLink(RestUtil.createSelfLink(uriInfo, department.getId(), DepartmentController.class));
                departmentResponseWrapper.getDepartments().add(departmentResponse);
            }
            for (Link link : RestUtil.createPaginatedResourceLink(uriInfo, paginationBean, DepartmentService.getInstance().count())) {
                departmentResponseWrapper.addLink(link);
            }
            return buildResponse(departmentResponseWrapper, type);
        } catch (Exception e) {
            log.error("Exception occurred while getting all departments", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception occurred while getting all departments").build();
        }
    }

    private Response buildResponse(DepartmentResponseWrapper departmentResponseWrapper, String type) {
        if (type != null && type.equals("xml")) {
            return Response.ok(departmentResponseWrapper).type(MediaType.APPLICATION_XML).build();
        } else {
            return Response.ok(departmentResponseWrapper).type(MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getDepartment(@PathParam("id") Long id, @QueryParam("type") String type) {
        log.info("Getting department...");
        try {
            DepartmentDto department = DepartmentService.getInstance().read(id);
            if (department == null) {
                throw new ResourceNotFoundException("Department not found", 404, "https://www.example.com/errors/404");
            }
            DepartmentResponse departmentResponse = new DepartmentResponse();
            departmentResponse.setDepartmentDto(department);
            departmentResponse.addLink(RestUtil.createSelfLink(uriInfo, id, DepartmentController.class));
            return buildResponse(departmentResponse, type);
        } catch (Exception e) {
            log.error("Exception occurred while getting department", e);
            return new WebApplicationException(new ResourceNotFoundException("Exception occurred while getting department", 500, "https://www.example.com/errors/500")).getResponse();
        }
    }

    private Response buildResponse(DepartmentResponse departmentResponse, String type) {
        if (type != null && type.equals("xml")) {
            return Response.ok(departmentResponse).type(MediaType.APPLICATION_XML).build();
        } else {
            return Response.ok(departmentResponse).type(MediaType.APPLICATION_JSON).build();
        }
    }


    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createDepartment(DepartmentDto departmentDto) {
        log.info("Creating department...");
        try {
            DepartmentDto createdDepartment = DepartmentService.getInstance().save(departmentDto);
            if (createdDepartment == null) {
                log.error("Department not created");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Department not created").build();
            } else {
                log.info("Department created");
                URI departmentUri = RestUtil.getCreatedAtUriForPost(uriInfo, createdDepartment.getId());
                Link departmentLink = RestUtil.createSelfLink(uriInfo, createdDepartment.getId(), DepartmentController.class);

                DepartmentResponse departmentResponse = new DepartmentResponse();
                departmentResponse.setDepartmentDto(createdDepartment);
                departmentResponse.addLink(departmentLink);

                GenericEntity<DepartmentResponse> entity = new GenericEntity<>(departmentResponse) {
                };
                return Response.created(departmentUri).entity(entity).build();
            }
        } catch (Exception e) {
            log.error("Exception occurred while creating department", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception occurred while creating department").build();

        }

    }

    @PUT
    @Path("{id}")
    public Response updateDepartment(@PathParam("id") Long id, DepartmentDto departmentDto) {
        log.info("Updating department...");
        try {
            boolean updated = DepartmentService.getInstance().update(departmentDto, id);
            if (updated) {
                log.error("Department not updated");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Department not updated").build();
            } else {
                log.info("Department updated");
                return getResponse(id, departmentDto);
            }
        } catch (Exception e) {
            log.error("Exception occurred while updating department", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception occurred while updating department").build();
        }
    }

    private Response getResponse(Long id, DepartmentDto departmentDto) {
        Link departmentLink = RestUtil.createSelfLink(uriInfo, id, DepartmentController.class);

        DepartmentResponse departmentResponse = new DepartmentResponse();
        departmentResponse.setDepartmentDto(departmentDto);
        departmentResponse.addLink(departmentLink);

        GenericEntity<DepartmentResponse> entity = new GenericEntity<>(departmentResponse) {
        };
        return Response.ok(entity).build();
    }


    @PATCH
    @Path("{id}")
    public Response patchDepartment(@PathParam("id") Long id, DepartmentDto departmentDto) {
        log.info("Patching department...");
        try {
            boolean updated = DepartmentService.getInstance().update(departmentDto, id);
            if (updated) {
                log.error("Department not patched");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Department not patched").build();
            } else {
                log.info("Department patched");
                return getResponse(id, departmentDto);
            }
        } catch (Exception e) {
            log.error("Exception occurred while patching department", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception occurred while patching department").build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteDepartment(@PathParam("id") Long id) {
        log.info("Deleting department...");
        try {
            DepartmentService.getInstance().delete(id);
            log.info("Department deleted");
            Map<String, String> response = Map.of("message", "Department deleted successfully");
            GenericEntity<Map<String, String>> entity = new GenericEntity<>(response) {
            };
            return Response.ok(entity).build();
        } catch (Exception e) {
            log.error("Exception occurred while deleting department", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception occurred while deleting department").build();
        }
    }

    @PATCH
    @Path("{dId}/manager/{mId}")
    public Response assignManager(@PathParam("dId") Long departmentId, @PathParam("mId") Long managerId) {
        return getResponse(departmentId, managerId);
    }

    private Response getResponse(Long departmentId, Long managerId) {
        log.info("Assigning manager to department...");
        try {
            boolean assigned = DepartmentService.getInstance().updateManager(departmentId, managerId);
            if (!assigned) {
                log.error("Manager not assigned to department");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Manager not assigned to department").build();
            } else {
                Map<String, String> response = Map.of("message", "Manager assigned to department successfully");
                GenericEntity<Map<String, String>> entity = new GenericEntity<>(response) {
                };
                return Response.ok(entity).build();
            }
        } catch (Exception e) {
            log.error("Exception occurred while assigning manager to department", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception occurred while assigning manager to department").build();
        }
    }

    @PUT
    @Path("{dId}/manager/{mId}")
    public Response assignManagerPut(@PathParam("dId") Long departmentId, @PathParam("mId") Long managerId) {
        return getResponse(departmentId, managerId);
    }


    @GET
    @Path("{id}/employees")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getEmployees(@PathParam("id") Long id, @BeanParam PaginationBean paginationBean, @QueryParam("type") String type) {
        log.info("Getting employees of department...");
        try {
            List<EmployeeProjection> employees = DepartmentService.getInstance().getEmployees(id, paginationBean.getOffset(), paginationBean.getLimit());
            EmployeeResponseWrapper employeeResponseWrapper = new EmployeeResponseWrapper();
            for (EmployeeProjection employee : employees) {
                EmployeeResponse employeeResponse = new EmployeeResponse();
                employeeResponse.setEmployee(employee);
                employeeResponse.addLink(RestUtil.createSelfLink(uriInfo, employee.getId(), EmployeeController.class));
                employeeResponseWrapper.getEmployees().add(employeeResponse);
            }
            for (Link link : RestUtil.createPaginatedResourceLink(uriInfo, paginationBean, DepartmentService.getInstance().countEmployees(id))) {
                employeeResponseWrapper.addLink(link);
            }
            return buildResponse(employeeResponseWrapper, type);
        } catch (Exception e) {
            log.error("Exception occurred while getting employees of department", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception occurred while getting employees of department").build();
        }
    }

    private Response buildResponse(EmployeeResponseWrapper employeeResponseWrapper, String type) {
        if (type != null && type.equals("xml")) {
            return Response.ok(employeeResponseWrapper).type(MediaType.APPLICATION_XML).build();
        } else {
            return Response.ok(employeeResponseWrapper).type(MediaType.APPLICATION_JSON).build();
        }
    }
}
