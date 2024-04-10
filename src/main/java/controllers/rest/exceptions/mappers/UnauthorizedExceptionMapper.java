package controllers.rest.exceptions.mappers;

import controllers.rest.beans.ErrorMessage;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<NotAuthorizedException> {

    @Override
    public Response toResponse(NotAuthorizedException exception) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .errorCode(Response.Status.UNAUTHORIZED.getStatusCode())
                .message(exception.getMessage())
                .documentation("https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html")
                .build();

        return Response.status(Response.Status.UNAUTHORIZED).entity(errorMessage).build();
    }
}