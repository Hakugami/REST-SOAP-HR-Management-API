package controllers.rest.exceptions.mappers;

import controllers.rest.beans.ErrorMessage;
import jakarta.ws.rs.NotAllowedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class MethodNotAllowedExceptionMapper implements ExceptionMapper<NotAllowedException> {

    @Override
    public Response toResponse(NotAllowedException exception) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .errorCode(Response.Status.METHOD_NOT_ALLOWED.getStatusCode())
                .message(exception.getMessage())
                .documentation("https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html")
                .build();

        return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity(errorMessage).build();
    }
}