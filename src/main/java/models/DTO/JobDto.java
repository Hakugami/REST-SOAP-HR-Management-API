package models.DTO;

import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;
import models.enums.JobTitle;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XmlRootElement
@JsonbNillable
public class JobDto extends BaseDTO {
    Long id;
    @XmlEnumValue(value = "title")
    JobTitle title;
    String description;
    @PositiveOrZero(message = "Starting salary should be positive or zero")
    BigDecimal startingSalary;
    @PositiveOrZero(message = "Max salary should be positive or zero")
    BigDecimal maxSalary;
    @PositiveOrZero(message = "Min experience should be positive or zero")
    Integer minExperience;
    Boolean available;
}
