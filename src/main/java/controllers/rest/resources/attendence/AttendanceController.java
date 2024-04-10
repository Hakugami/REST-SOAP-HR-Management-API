package controllers.rest.resources.attendence;

import com.nimbusds.jwt.JWTClaimsSet;
import controllers.rest.annotations.Secured;
import controllers.rest.beans.PaginationBean;
import controllers.rest.helpers.utils.RestUtil;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.*;
import lombok.extern.slf4j.Slf4j;
import models.DTO.AttendanceDto;
import models.enums.AttendanceStatus;
import models.enums.Privilege;
import services.impl.AttendanceService;
import services.impl.EmployeeService;
import utils.ApiUtil;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Path("attendance")
@Secured(value = Privilege.EMPLOYEE)
public class AttendanceController {

    @Context
    private ContainerRequestContext requestContext;

    @Context
    private UriInfo uriInfo;

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
            Map<String, String> responseMap = Map.of("message", "Attendance marked successfully", "status", attendanceStatus.toString());
            GenericEntity<Map<String, String>> entity = new GenericEntity<>(responseMap) {
            };
            return Response.ok().entity(entity).build();
        } else {
            Map<String, String> responseMap = Map.of("message", "Attendance marking failed", "status", attendanceStatus.toString());
            GenericEntity<Map<String, String>> entity = new GenericEntity<>(responseMap) {
            };
            return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Secured(Privilege.HR)
    public Response getAttendance(@BeanParam PaginationBean paginationBean, @QueryParam("type") String type) {
        List<AttendanceDto> attendances = AttendanceService.getInstance().readAll(paginationBean.getOffset(), paginationBean.getLimit());
        AttendanceResponseWrapper attendanceResponseWrapper = new AttendanceResponseWrapper();
        attendances.forEach(attendanceDto -> {
            AttendanceResponse attendanceResponse = new AttendanceResponse();
            attendanceResponse.setAttendanceDto(attendanceDto);
            attendanceResponseWrapper.addLink(RestUtil.createSelfLink(uriInfo, attendanceDto.getId(), AttendanceController.class));
        });
        for (Link link : RestUtil.createPaginatedResourceLink(uriInfo, paginationBean, AttendanceService.getInstance().count())) {
            attendanceResponseWrapper.addLink(link);
        }
        return buildResponse(attendanceResponseWrapper, type);
    }

    private Response buildResponse(AttendanceResponseWrapper attendanceResponseWrapper, String type) {
        if (type != null && type.equals("xml")) {
            return Response.ok(attendanceResponseWrapper).type(MediaType.APPLICATION_XML).build();
        } else {
            return Response.ok(attendanceResponseWrapper).type(MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("/{id}")
    @Secured(Privilege.HR)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAttendanceById(@PathParam("id") Long id, @QueryParam("type") String type) {
        AttendanceDto attendanceDto = AttendanceService.getInstance().read(id);
        if (attendanceDto == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Attendance not found").build();
        }
        AttendanceResponse attendanceResponse = new AttendanceResponse();
        attendanceResponse.setAttendanceDto(attendanceDto);
        attendanceResponse.addLink(RestUtil.createSelfLink(uriInfo, attendanceDto.getId(), AttendanceController.class));
        return buildResponse(attendanceResponse, type);
    }

    private Response buildResponse(AttendanceResponse attendanceResponse, String type) {
        if (type != null && type.equals("xml")) {
            return Response.ok(attendanceResponse).type(MediaType.APPLICATION_XML).build();
        } else {
            return Response.ok(attendanceResponse).type(MediaType.APPLICATION_JSON).build();
        }
    }
}