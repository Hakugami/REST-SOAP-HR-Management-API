package controllers.soap.Endpoints.attendance;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.ws.BindingType;
import lombok.extern.slf4j.Slf4j;
import models.DTO.AttendanceDto;
import models.enums.AttendanceStatus;
import services.impl.AttendanceService;
import services.impl.EmployeeService;

import java.time.LocalTime;
import java.util.List;

@WebService
@Slf4j
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
@BindingType(jakarta.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AttendanceWebService {

    @WebMethod
    public String markAttendance(@WebParam(name = "email") String email) {
        AttendanceStatus attendanceStatus = determineAttendanceStatus();
        boolean result = markAttendance(email, attendanceStatus);
        if (result) {
            return "Attendance marked as " + attendanceStatus;
        } else {
            return "An error occurred while marking attendance";
        }
    }

    @WebMethod
    public List<AttendanceDto> getAttendance(@WebParam(name = "offset") int offset, @WebParam(name = "limit") int limit) {
        return AttendanceService.getInstance().readAll(offset, limit);
    }

    @WebMethod
    public AttendanceDto getAttendanceById(@WebParam(name = "id") Long id) {
        return AttendanceService.getInstance().read(id);
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

    private boolean markAttendance(String email, AttendanceStatus attendanceStatus) {
        return EmployeeService.getInstance().attendance(email, attendanceStatus);
    }
}
