package controllers.filters;

import com.nimbusds.jwt.JWTClaimsSet;
import controllers.rest.annotations.Secured;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import models.enums.Privilege;
import utils.JWTUtil;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;

@Slf4j
@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final JWTUtil jwtUtil = new JWTUtil();
    @Context
    private ResourceInfo resourceInfo;

    @Context
    private UriInfo uriInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();
        Class<?> resourceClass = resourceInfo.getResourceClass();

        Privilege requiredPrivilege = Privilege.ALL; // default privilege

        if (resourceClass.isAnnotationPresent(Secured.class)) {
            requiredPrivilege = resourceClass.getAnnotation(Secured.class).value();
        }
        if (method.isAnnotationPresent(Secured.class)) {
            requiredPrivilege = method.getAnnotation(Secured.class).value();
        }

        // If the required privilege is NONE, allow the request to proceed without checking the JWT token
        if (requiredPrivilege == Privilege.ALL) {
            return;
        }

        String authorizationHeader = requestContext.getHeaderString(AUTHORIZATION_HEADER);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            redirectToLogin(requestContext);
            return;
        }

        String token = authorizationHeader.substring("Bearer ".length()).trim();
        try {
            log.info("Required privilege: {}", requiredPrivilege);
            JWTClaimsSet claimsSet = jwtUtil.verifyJWT(token, requiredPrivilege);
            if (claimsSet == null) {
                redirectToLogin(requestContext);
            } else {
                Privilege tokenPrivilege = Privilege.valueOf(claimsSet.getClaim("privileges").toString());
                log.info("Token privilege: {}", tokenPrivilege);
                if (tokenPrivilege.compareTo(requiredPrivilege) < 0) {
                    redirectToLogin(requestContext);
                }
            }
        } catch (Exception e) {
            redirectToLogin(requestContext);
        }
    }

    private void redirectToLogin(ContainerRequestContext requestContext) {
        UriBuilder builder = uriInfo.getBaseUriBuilder().path("auth/login");
        URI loginUri = builder.build();
        String message = "Unauthorized access. Please login at: " + loginUri.toString();
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(message).build());
    }
}