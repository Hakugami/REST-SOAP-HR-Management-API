package models.DTO;

import controllers.soap.adapters.LocalDateAdapter;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.*;
import models.enums.AttendanceStatus;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XmlRootElement
@JsonbNillable
@XmlAccessorType(XmlAccessType.FIELD)
public class AttendanceDto extends BaseDTO {
    Long id;
    @NotNull(message = "Start Date cannot be null")
    @JsonbDateFormat("yyyy-MM-dd")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    LocalDate attendanceTime;
    @NotNull(message = "Status cannot be null")
    @XmlEnumValue(value = "status")
    AttendanceStatus status;
    String employeeName;
}
