package controllers.rest.exceptions.mappers;

import controllers.rest.beans.ErrorMessage;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .errorCode(Response.Status.NOT_FOUND.getStatusCode())
                .message(exception.getMessage())
                .documentation("https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html")
                .build();
        return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
    }
}