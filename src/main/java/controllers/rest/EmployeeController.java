package controllers.rest;

import controllers.rest.annotations.Secured;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
public class EmployeeController {

    @GET
    @Secured
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getEmployees(@QueryParam("type") String type, @QueryParam("fields") String fields, @QueryParam("offset") @DefaultValue("0") int offset, @QueryParam("limit") @DefaultValue("10") int limit) {
        Set<String> fieldSet = ApiUtil.getFields(fields);
        log.info("Fields: {}", fieldSet);
        List<EmployeeProjection> employees = EmployeeService.getInstance().getAllEmployeesPartialResponse(fieldSet, offset, limit);
        return buildResponse(employees, type);
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getEmployeePartialResponse(@PathParam("id") Long id, @QueryParam("fields") String fields, @QueryParam("type") String type) {
        Set<String> fieldSet = ApiUtil.getFields(fields);
        log.info("Fields: {}", fieldSet);
        EmployeeProjection employee = EmployeeService.getInstance().employeePartialResponse(id, fieldSet);
        return buildResponse(employee, type);
    }

    private Response buildResponse(Object entity, String type) {
        if (entity instanceof EmployeeProjection) {
            GenericEntity<EmployeeProjection> genericEntity = new GenericEntity<EmployeeProjection>((EmployeeProjection) entity) {
            };
            return buildResponseWithEntity(genericEntity, type);
        } else if (entity instanceof List) {
            GenericEntity<List<EmployeeProjection>> genericEntity = new GenericEntity<List<EmployeeProjection>>((List<EmployeeProjection>) entity) {
            };
            return buildResponseWithEntity(genericEntity, type);
        } else {
            log.error("Invalid entity type: " + entity.getClass().getName());
            return Response.serverError().build();
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
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createEmployee(@Valid EmployeeDto employeeDto) {
        if ((employeeDto.getHireDate() != null && employeeDto.getHireDate().isAfter(LocalDate.now())) || (employeeDto.getBirthDate() != null && employeeDto.getBirthDate().isAfter(LocalDate.now()))) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Hire date and birth date should be in the past or present").build();
        }
        EmployeeDto saved = AuthenticationService.getInstance().save(employeeDto);
        return Response.ok(saved).type(MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateEmployee(@PathParam("id") Long id,  EmployeeDto employeeDto) {
        return getResponse(id, employeeDto);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteEmployee(@PathParam("id") Long id, @QueryParam("isFired") @DefaultValue("false") boolean isFired) {
        log.info("Deleting employee with id: {} isFired: {}", id, isFired);
        boolean deleted = EmployeeService.getInstance().delete(id, isFired);
        if (deleted) {
            return Response.ok().build();
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
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}