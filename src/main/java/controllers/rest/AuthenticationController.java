package controllers.rest;

import controllers.rest.annotations.Secured;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.DTO.LoginDto;
import models.enums.Privilege;
import services.impl.AuthenticationService;

@Path("/auth")
@Secured(value = Privilege.NONE)
public class AuthenticationController {

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginDto loginRequest) {
        String loginToken = AuthenticationService.getInstance().login(loginRequest.getUsername(), loginRequest.getPassword());
        if (loginToken == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok().header("Authorization", "Bearer " + loginToken).build();
    }
}