package controllers.rest.exceptions.mappers;

import controllers.rest.exceptions.custom.EntityNotCreatedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class EntityNotCreatedExceptionMapper implements ExceptionMapper<EntityNotCreatedException> {
    @Override
    public Response toResponse(EntityNotCreatedException exception) {
        return Response.status(Response.Status.NOT_MODIFIED).entity(exception.getErrorMessage()).build();
    }
}
