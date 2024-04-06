package models.DTO;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;
import models.enums.JobTitle;
import models.enums.Privilege;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for {@link models.entities.Employee}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XmlRootElement
public class EmployeeDto extends BaseDTO implements Serializable {
    Long id;
    String username;
    String password;
    String firstName;
    String lastName;
    String email;
    String phone;

    @JsonbDateFormat("yyyy-MM-dd")
    LocalDate birthDate;

    @JsonbDateFormat("yyyy-MM-dd")
    LocalDate hireDate;

    @JsonbDateFormat("yyyy-MM-dd")
    LocalDate fireDate;

    BigDecimal salary;
    Boolean isHired;
    Integer yearsOfExperience;
    Privilege privilege;
    Integer vacationDays;
    private JobTitle jobTitle;
    private Double deduction;
}
