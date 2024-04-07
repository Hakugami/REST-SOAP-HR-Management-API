package controllers.filters;

import controllers.rest.annotations.Secured;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.lang.reflect.Method;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println("Security filter");
        Method method = resourceInfo.getResourceMethod();
        if (method.isAnnotationPresent(Secured.class) || resourceInfo.getResourceClass().isAnnotationPresent(Secured.class)) {
            String authHeader = requestContext.getHeaderString(AUTHORIZATION_HEADER);
            System.out.println("Trying to authenticate with token: " + authHeader )  ;
            if (authHeader == null || !isValidToken(authHeader)) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        }
    }

    private boolean isValidToken(String token) {
        // Validate the token. This is just a placeholder. In a real application, you would
        // check the token against your database or another persistent storage.
        return "valid_token".equals(token);
    }
}