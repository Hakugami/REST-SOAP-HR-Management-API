package controllers.rest;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import models.enums.AttendanceStatus;
import services.impl.EmployeeService;

import java.time.LocalTime;

@Slf4j
@Path("attendance")
public class AttendanceController {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response markAttendance(String email) {
        if (email == null || email.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Email is required").build();
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
}