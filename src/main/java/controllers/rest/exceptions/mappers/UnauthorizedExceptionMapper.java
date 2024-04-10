package controllers.rest.exceptions.mappers;

import controllers.rest.beans.ErrorMessage;
import controllers.rest.exceptions.custom.UnauthorizedException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {

    @Override
    public Response toResponse(UnauthorizedException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getErrorMessage().getMessage(), exception.getErrorMessage().getErrorCode(), exception.getErrorMessage().getDocumentation());
        return Response.status(Response.Status.UNAUTHORIZED).entity(errorMessage).build();
    }
}