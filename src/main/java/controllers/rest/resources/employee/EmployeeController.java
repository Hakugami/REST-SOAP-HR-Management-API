package controllers.rest.resources.employee;

import controllers.rest.annotations.Secured;
import controllers.rest.beans.PaginationBean;
import controllers.rest.helpers.utils.RestUtil;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import lombok.extern.slf4j.Slf4j;
import models.DTO.EmployeeDto;
import models.enums.Privilege;
import persistence.repositories.helpers.projections.EmployeeProjection;
import services.impl.AuthenticationService;
import services.impl.EmployeeService;
import utils.ApiUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Path("employees")
@Slf4j
@Secured(value = Privilege.ALL)
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class EmployeeController {

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/{id}")
    public Response getEmployeePartialResponse(@PathParam("id") Long id, @QueryParam("fields") String fields, @QueryParam("type") String type) {
        Set<String> fieldSet = ApiUtil.getFields(fields);
        log.info("Fields: {}", fieldSet);
        EmployeeProjection employee = EmployeeService.getInstance().employeePartialResponse(id, fieldSet);
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setEmployee(employee);
        employeeResponse.addLink(RestUtil.createSelfLink(uriInfo, id, EmployeeController.class));
        return buildResponse(employeeResponse, type);
    }

    @GET
    public Response getEmployees(@QueryParam("type") String type, @QueryParam("fields") String fields, @BeanParam PaginationBean paginationBean) {
        Set<String> fieldSet = ApiUtil.getFields(fields);
        log.info("Fields: {}", fieldSet);
        List<EmployeeProjection> employees = EmployeeService.getInstance().getAllEmployeesPartialResponse(fieldSet, paginationBean.getOffset(), paginationBean.getLimit());
        EmployeeResponseWrapper employeeResponseWrapper = new EmployeeResponseWrapper();
        for (EmployeeProjection employee : employees) {
            EmployeeResponse employeeResponse = new EmployeeResponse();
            employeeResponse.setEmployee(employee);
            employeeResponse.addLink(RestUtil.createSelfLink(uriInfo, employee.getId(), EmployeeController.class));
            employeeResponseWrapper.getEmployees().add(employeeResponse);
        }
        for (Link link : RestUtil.createPaginatedResourceLink(uriInfo, paginationBean, EmployeeService.getInstance().count())) {
            employeeResponseWrapper.addLink(link);
        }
        return buildResponse(employeeResponseWrapper, type);
    }

    private Response buildResponse(Object entity, String type) {
        switch (entity) {
            case EmployeeProjection employeeProjection -> {
                GenericEntity<EmployeeProjection> genericEntity = new GenericEntity<EmployeeProjection>((EmployeeProjection) entity) {
                };
                return buildResponseWithEntity(genericEntity, type);
            }
            case List list -> {
                GenericEntity<List<EmployeeProjection>> genericEntity = new GenericEntity<List<EmployeeProjection>>((List<EmployeeProjection>) entity) {
                };
                return buildResponseWithEntity(genericEntity, type);
            }
            case EmployeeResponse employeeResponse -> {
                GenericEntity<EmployeeResponse> genericEntity = new GenericEntity<EmployeeResponse>((EmployeeResponse) entity) {
                };
                return buildResponseWithEntity(genericEntity, type);
            }
            case EmployeeResponseWrapper employeeResponseWrapper -> {
                GenericEntity<EmployeeResponseWrapper> genericEntity = new GenericEntity<EmployeeResponseWrapper>((EmployeeResponseWrapper) entity) {
                };
                return buildResponseWithEntity(genericEntity, type);
            }
            case null, default -> {
                log.error("Invalid entity type: {}", entity.getClass().getName());
                return Response.serverError().build();
            }
        }
    }

    private <T> Response buildResponseWithEntity(GenericEntity<T> entity, String type) {
        if ("json".equalsIgnoreCase(type)) {
            return Response.ok(entity).type(MediaType.APPLICATION_JSON).build();
        } else if ("xml".equalsIgnoreCase(type)) {
            return Response.ok(entity).type(MediaType.APPLICATION_XML).build();
        } else {
            return Response.ok(entity).type(MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    public Response createEmployee(@Valid EmployeeDto employeeDto) {
        if ((employeeDto.getHireDate() != null
                && employeeDto.getHireDate().isAfter(LocalDate.now()))
                || (employeeDto.getBirthDate() != null
                && employeeDto.getBirthDate().isAfter(LocalDate.now()))) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Hire date and birth date should be in the past or present").build();
        }
        EmployeeDto saved = AuthenticationService.getInstance().save(employeeDto);
        return Response.ok(saved).type(MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateEmployee(@PathParam("id") Long id, EmployeeDto employeeDto) {
        return getResponse(id, employeeDto);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteEmployee(@PathParam("id") Long id, @QueryParam("isFired") @DefaultValue("false") boolean isFired) {
        log.info("Deleting employee with id: {} isFired: {}", id, isFired);
        boolean deleted = EmployeeService.getInstance().delete(id, isFired);
        if (deleted) {
            return Response.ok("Employee left the company").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @PATCH
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response partialUpdateEmployee(@PathParam("id") Long id, @Valid EmployeeDto employeeDto) {
        return getResponse(id, employeeDto);
    }

    private Response getResponse(Long id, EmployeeDto employeeDto) {
        if ((employeeDto.getHireDate() != null && employeeDto.getHireDate().isAfter(LocalDate.now())) || (employeeDto.getBirthDate() != null && employeeDto.getBirthDate().isAfter(LocalDate.now()))) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Hire date and birth date should be in the past or present").build();
        }
        boolean updated = EmployeeService.getInstance().update(employeeDto, id);
        if (updated) {
            Link selfLink = RestUtil.createSelfLink(uriInfo, id, EmployeeController.class);
            return Response.ok("Updated Successfully").links(selfLink).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @PUT
    @Path("/{eid}/manager/{mid}")
    public Response assignManager(@PathParam("eid") Long employeeId, @PathParam("mid") Long managerId) {
        try {
            log.info("Assigning manager with id: {} to employee with id: {}", managerId, employeeId);
            boolean assigned = EmployeeService.getInstance().assignManager(employeeId, managerId);
            if (!assigned) {
                return Response.status(Response.Status.NOT_FOUND).entity("Failed to assign manager").build();
            } else {
                Link selfLink = RestUtil.createSelfLink(uriInfo, employeeId, EmployeeController.class);
                return Response.ok("Updated Successfully").links(selfLink).build();
            }
        } catch (Exception e) {
            log.error("Exception occurred while assigning manager", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception occurred while assigning manager").build();
        }
    }


    @GET
    @Path("{id}/manager")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getManager(@PathParam("id") Long id, @QueryParam("type") String type) {
        log.info("Getting manager for employee with id: {}", id);
        EmployeeProjection manager = EmployeeService.getInstance().getManager(id);
        if (manager == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Manager not found").build();
        } else {
            EmployeeResponse employeeResponse = new EmployeeResponse();
            employeeResponse.setEmployee(manager);
            employeeResponse.addLink(RestUtil.createSelfLink(uriInfo, id, EmployeeController.class));
            return buildResponse(employeeResponse, type);
        }
    }

}