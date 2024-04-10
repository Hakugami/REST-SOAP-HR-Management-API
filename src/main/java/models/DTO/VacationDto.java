package models.DTO;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.validation.constraints.NotEmpty;
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
public class VacationDto extends BaseDTO implements Serializable {
    Long id;
    @JsonbDateFormat("yyyy-MM-dd")
    LocalDate startDate;
    @JsonbDateFormat("yyyy-MM-dd")
    LocalDate endDate;
    @NotEmpty(message = "Reason cannot be empty")
    String reason;
    Boolean isApproved;
}