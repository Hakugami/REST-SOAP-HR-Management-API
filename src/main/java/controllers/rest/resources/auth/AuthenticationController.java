package controllers.rest.resources.auth;

import controllers.rest.annotations.Secured;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.DTO.LoginDto;
import models.enums.Privilege;
import services.impl.AuthenticationService;

import java.util.Map;

@Path("/auth")
@Secured(value = Privilege.ALL)
public class AuthenticationController {

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginDto loginRequest) {
        String loginToken = AuthenticationService.getInstance().login(loginRequest.getUsername(), loginRequest.getPassword());
        if (loginToken == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        Map<String, String> message = Map.of("message", "Login successful");
        GenericEntity<Map<String, String>> entity = new GenericEntity<>(message) {
        };
        return Response.ok().header("Authorization", "Bearer " + loginToken).entity(entity).build();
    }
}