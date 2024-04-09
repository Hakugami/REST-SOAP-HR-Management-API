package controllers.rest.resources.manager;

import controllers.rest.beans.PaginationBean;
import controllers.rest.helpers.utils.RestUtil;
import controllers.rest.resources.employee.EmployeeResponse;
import controllers.rest.resources.employee.EmployeeResponseWrapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import lombok.extern.slf4j.Slf4j;
import models.DTO.EmployeeDto;
import persistence.repositories.helpers.projections.EmployeeProjection;
import services.impl.EmployeeService;
import services.impl.ManagerService;
import utils.ApiUtil;

import java.util.List;
import java.util.Set;

@Slf4j
@Path("managers")
public class ManagerController {
    @Context
    private UriInfo uriInfo;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getManagers(@BeanParam PaginationBean paginationBean, @QueryParam("fields") String fields, @QueryParam("type") String type) {
        Set<String> fieldsSet = ApiUtil.getFields(fields);
        log.info("Fields: {}", fieldsSet);
        List<EmployeeProjection> managers = ManagerService.getInstance().getAllManagersPartialResponse(fieldsSet, paginationBean.getOffset(), paginationBean.getLimit());
        EmployeeResponseWrapper employeeResponseWrapper = new EmployeeResponseWrapper();
        for (EmployeeProjection manager : managers) {
            EmployeeResponse employeeResponse = new EmployeeResponse();
            employeeResponse.setEmployee(manager);
            employeeResponse.addLink(RestUtil.createSelfLink(uriInfo, manager.getId(), ManagerController.class));
            employeeResponseWrapper.getEmployees().add(employeeResponse);
        }
        for (Link link : RestUtil.createPaginatedResourceLink(uriInfo, paginationBean, ManagerService.getInstance().count())) {
            employeeResponseWrapper.addLink(link);
        }
        return buildResponse(employeeResponseWrapper, type);
    }

    private Response buildResponse(EmployeeResponseWrapper employeeResponseWrapper, String type) {
        if (type != null && type.equals("xml")) {
            return Response.ok(employeeResponseWrapper).type(MediaType.APPLICATION_XML).build();
        } else {
            return Response.ok(employeeResponseWrapper).type(MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getManagerPartialResponse(@PathParam("id") Long id, @QueryParam("fields") String fields, @QueryParam("type") String type) {
        Set<String> fieldSet = ApiUtil.getFields(fields);
        log.info("Fields: {}", fieldSet);
        EmployeeProjection manager = ManagerService.getInstance().getManagerPartialResponse(id, fieldSet);
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setEmployee(manager);
        employeeResponse.addLink(RestUtil.createSelfLink(uriInfo, id, ManagerController.class));
        return buildResponse(employeeResponse, type);
    }

    private Response buildResponse(EmployeeResponse employeeResponse, String type) {
        if (type != null && type.equals("xml")) {
            return Response.ok(employeeResponse).type(MediaType.APPLICATION_XML).build();
        } else {
            return Response.ok(employeeResponse).type(MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createManager(EmployeeDto employeeDto) {
        log.info("Creating manager...");
        EmployeeProjection manager = ManagerService.getInstance().saveAndReturnProjection(employeeDto);
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setEmployee(manager);
        employeeResponse.addLink(RestUtil.createSelfLink(uriInfo, manager.getId(), ManagerController.class));
        return Response.created(uriInfo.getRequestUri()).entity(employeeResponse).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateManager(@PathParam("id") Long id, EmployeeDto employeeDto) {
        log.info("Updating manager...");
        try {
            boolean updated = ManagerService.getInstance().update(employeeDto, id);
            if (!updated) {
                log.error("Failed to update manager");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to update manager").build();
            } else {
                log.info("Manager updated successfully");
                return getManagerPartialResponse(id, null, null);
            }
        } catch (Exception e) {
            log.error("Exception occurred while updating manager", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception occurred while updating manager").build();
        }
    }

    @PATCH
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response partialUpdateManager(@PathParam("id") Long id, EmployeeDto employeeDto) {
        log.info("Partially updating manager...");
        try {
            boolean updated = ManagerService.getInstance().update(employeeDto, id);
            if (!updated) {
                log.error("Failed to partially update manager");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to partially update manager").build();
            } else {
                log.info("Manager partially updated successfully");
                return getManagerPartialResponse(id, null, null);
            }
        } catch (Exception e) {
            log.error("Exception occurred while partially updating manager", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception occurred while partially updating manager").build();
        }
    }


    @DELETE
    @Path("/{id}")
    public Response deleteManager(@PathParam("id") Long id) {
        log.info("Deleting manager...");
        try {
            boolean deleted = EmployeeService.getInstance().delete(id, true);
            if (!deleted) {
                log.error("Failed to delete manager");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to delete manager").build();
            } else {
                log.info("Manager deleted successfully");
                return Response.ok().build();
            }
        } catch (Exception e) {
            log.error("Exception occurred while deleting manager", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception occurred while deleting manager").build();
        }
    }


}
