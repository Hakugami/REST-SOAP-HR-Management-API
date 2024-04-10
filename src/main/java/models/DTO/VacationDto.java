package models.DTO;

import controllers.soap.adapters.LocalDateAdapter;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import models.entities.Vacation;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Vacation}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class VacationDto extends BaseDTO implements Serializable {
    Long id;
    @JsonbDateFormat("yyyy-MM-dd")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    LocalDate startDate;
    @JsonbDateFormat("yyyy-MM-dd")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    LocalDate endDate;
    @NotEmpty(message = "Reason cannot be empty")
    String reason;
    Boolean isApproved;
}