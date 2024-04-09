package controllers.rest.exceptions.mappers;

import controllers.rest.exceptions.custom.UnauthorizedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException>{

        @Override
        public Response toResponse(UnauthorizedException exception) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(exception.getErrorMessage()).build();
        }
}
