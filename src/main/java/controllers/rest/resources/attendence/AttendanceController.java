package controllers.rest.resources.attendence;

import com.nimbusds.jwt.JWTClaimsSet;
import controllers.rest.annotations.Secured;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import models.enums.AttendanceStatus;
import models.enums.Privilege;
import controllers.rest.beans.PaginationBean;
import services.impl.EmployeeService;
import utils.ApiUtil;

import java.time.LocalTime;

@Slf4j
@Path("attendance")
@Secured(value = Privilege.EMPLOYEE)
public class AttendanceController {

    @Context
    private ContainerRequestContext requestContext;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response markAttendance() {
        JWTClaimsSet claimsSet = ApiUtil.getClaimsSet(requestContext, Privilege.EMPLOYEE);
        String email;
        if (claimsSet != null) {
            email = claimsSet.getClaim("email").toString();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build();
        }

        AttendanceStatus attendanceStatus = determineAttendanceStatus();
        return markAttendanceAndGenerateResponse(email, attendanceStatus);
    }

    private AttendanceStatus determineAttendanceStatus() {
        LocalTime now = LocalTime.now();
        LocalTime startLateTime = LocalTime.of(9, 0); // 9:00 AM
        LocalTime endLateTime = LocalTime.of(9, 30); // 9:30 AM

        if (now.isBefore(startLateTime)) {
            // It's before 9:00 AM
            return AttendanceStatus.PRESENT;
        } else if (now.isBefore(endLateTime)) {
            // It's between 9:00 AM and 9:30 AM
            return AttendanceStatus.LATE;
        } else {
            // It's after 9:30 AM
            return AttendanceStatus.ABSENT;
        }
    }

    private Response markAttendanceAndGenerateResponse(String email, AttendanceStatus attendanceStatus) {
        boolean result = EmployeeService.getInstance().attendance(email, attendanceStatus);
        if (result) {
            return Response.status(Response.Status.OK).entity("Attendance marked as " + attendanceStatus).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while marking attendance").build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAttendance(@BeanParam PaginationBean paginationBean, @QueryParam("type") String type){
        return Response.ok(EmployeeService.getInstance().readAll(paginationBean.getOffset(), paginationBean.getLimit(), null)).build();
    }
}