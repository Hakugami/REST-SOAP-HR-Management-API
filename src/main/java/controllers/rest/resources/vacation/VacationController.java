package controllers.rest.resources.vacation;


import com.nimbusds.jwt.JWTClaimsSet;
import controllers.rest.annotations.Secured;
import controllers.rest.exceptions.custom.UnauthorizedException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import models.DTO.VacationDto;
import models.enums.Privilege;
import services.impl.VacationService;
import utils.ApiUtil;

@Path("vacations")
@Slf4j
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class VacationController {


    @Context
    private ContainerRequestContext requestContext;

    @POST
    @Secured(Privilege.EMPLOYEE)
    public Response  requestVacation(VacationDto vacationDto)  {
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
                return Response.status(Response.Status.CREATED).entity("Vacation requested successfully").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Vacation request failed").build();
            }
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("{id}/approve")
    public Response approveVacationRequest(@PathParam("id") Long id) {
        boolean result = VacationService.getInstance().approveVacation(id);
        if (result) {
            return Response.status(Response.Status.OK).entity("Vacation request approved").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Vacation request approval failed").build();
        }
    }

    @POST
    @Path("{id}/reject")
    public Response rejectVacationRequest(@PathParam("id") Long id) {
        boolean result = VacationService.getInstance().rejectVacation(id);
        if (result) {
            return Response.status(Response.Status.OK).entity("Vacation request rejected").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Vacation request rejection failed").build();
        }
    }

}
