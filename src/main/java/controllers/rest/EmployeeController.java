package controllers.rest;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.DTO.EmployeeDto;
import services.impl.EmployeeService;


@Path("employees")
public class EmployeeController {

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getEmployees() {
        return Response.ok(EmployeeService.getInstance().readAll()).build();
    }

    @GET
    @Path("{extension}")
    public Response getEmployees(@PathParam("extension") String extension) {
        if ("json".equals(extension)) {
            return Response.ok(EmployeeService.getInstance().readAll()).type(MediaType.APPLICATION_JSON).build();
        } else if ("xml".equals(extension)) {
            return Response.ok(EmployeeService.getInstance().readAll()).type(MediaType.APPLICATION_XML).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createEmployee(EmployeeDto employeeDto) {
        EmployeeDto saved = EmployeeService.getInstance().save(employeeDto);
        return Response.ok(saved).build();
    }


}
