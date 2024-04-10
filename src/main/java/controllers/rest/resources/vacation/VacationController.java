package controllers.rest.resources.vacation;


import com.nimbusds.jwt.JWTClaimsSet;
import controllers.rest.annotations.Secured;
import controllers.rest.beans.PaginationBean;
import controllers.rest.helpers.utils.RestUtil;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.*;
import lombok.extern.slf4j.Slf4j;
import models.DTO.VacationDto;
import models.enums.Privilege;
import services.impl.VacationService;
import utils.ApiUtil;

import java.util.List;
import java.util.Map;

@Path("vacations")
@Slf4j
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class VacationController {


    @Context
    private ContainerRequestContext requestContext;

    @POST
    @Secured(Privilege.EMPLOYEE)
    public Response requestVacation(VacationDto vacationDto) {
        JWTClaimsSet claimsSet = ApiUtil.getClaimsSet(requestContext, Privilege.EMPLOYEE);
        String email;
        if (claimsSet != null) {
            email = claimsSet.getClaim("email").toString();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build();
        }
        try {
            boolean result = VacationService.getInstance().requestVacation(email, vacationDto);
            if (result) {
                Map<String, String> message = Map.of("message", "Vacation request successful");
                GenericEntity<Map<String, String>> entity = new GenericEntity<>(message) {
                };
                return Response.ok().entity(entity).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Vacation request failed").build();
            }
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("{id}/approve")
    @Secured(Privilege.MANAGER)
    public Response approveVacationRequest(@PathParam("id") Long id) {
        boolean result = VacationService.getInstance().approveVacation(id);
        if (result) {
            Map<String, String> message = Map.of("message", "Vacation request approved");
            GenericEntity<Map<String, String>> entity = new GenericEntity<>(message) {
            };
            return Response.ok().entity(entity).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Vacation request approval failed").build();
        }
    }

    @POST
    @Path("{id}/reject")
    @Secured(Privilege.MANAGER)
    public Response rejectVacationRequest(@PathParam("id") Long id) {
        boolean result = VacationService.getInstance().rejectVacation(id);
        if (result) {
            Map<String, String> message = Map.of("message", "Vacation request rejected");
            GenericEntity<Map<String, String>> entity = new GenericEntity<>(message) {
            };
            return Response.ok().entity(entity).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Vacation request rejection failed").build();
        }
    }

    @GET
    @Secured(Privilege.EMPLOYEE)
    public Response getVacations(@BeanParam PaginationBean paginationBean, @QueryParam("type") String type) {
        List<VacationDto> vacations = VacationService.getInstance().readAll(paginationBean.getOffset(), paginationBean.getLimit());
        VacationResponseWrapper vacationResponseWrapper = new VacationResponseWrapper();
        vacations.forEach(vacationDto -> {
            VacationResponse vacationResponse = new VacationResponse();
            vacationResponse.setVacationDto(vacationDto);
            vacationResponseWrapper.addLink(RestUtil.createSelfLink(requestContext.getUriInfo(), vacationDto.getId(), VacationController.class));
        });
        for (Link link : RestUtil.createPaginatedResourceLink(requestContext.getUriInfo(), paginationBean, VacationService.getInstance().count())) {
            vacationResponseWrapper.addLink(link);
        }
        return buildResponse(vacationResponseWrapper, type);
    }

    private Response buildResponse(Object entity, String type) {
        GenericEntity<?> genericEntity = new GenericEntity<>(entity) {
        };
        if (type != null && type.equals("xml")) {
            return Response.ok().entity(genericEntity).type(MediaType.APPLICATION_XML).build();
        } else {
            return Response.ok().entity(genericEntity).type(MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("{id}")
    @Secured(Privilege.EMPLOYEE)
    public Response getVacation(@PathParam("id") Long id, @QueryParam("type") String type) {
        VacationDto vacationDto = VacationService.getInstance().read(id);
        if (vacationDto == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Vacation not found").build();
        }
        VacationResponse vacationResponse = new VacationResponse();
        vacationResponse.setVacationDto(vacationDto);
        vacationResponse.addLink(RestUtil.createSelfLink(requestContext.getUriInfo(), id, VacationController.class));
        return buildResponse(vacationResponse, type);
    }


}
