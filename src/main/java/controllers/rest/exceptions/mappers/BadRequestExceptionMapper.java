package controllers.rest.exceptions.mappers;

import controllers.rest.beans.ErrorMessage;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

    @Override
    public Response toResponse(BadRequestException exception) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .errorCode(Response.Status.BAD_REQUEST.getStatusCode())
                .message(exception.getMessage())
                .documentation("https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html")
                .build();

        return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
    }
}