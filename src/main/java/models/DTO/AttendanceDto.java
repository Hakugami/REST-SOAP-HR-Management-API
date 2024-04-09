package models.DTO;

import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;
import models.enums.AttendanceStatus;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XmlRootElement
@JsonbNillable
public class AttendanceDto extends BaseDTO{
    Long id;
    @NotNull(message = "Start Date cannot be null")
    Date attendanceTime;
    @NotNull(message = "Status cannot be null")
    AttendanceStatus status;
    String employeeName;
}
