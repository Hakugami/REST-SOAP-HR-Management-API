package models.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import models.enums.JobTitle;
import models.enums.Privilege;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO for {@link models.entities.Employee}
 */
@EqualsAndHashCode(callSuper = true)
@Value
public class EmployeeDto extends BaseDTO implements Serializable {
    Long id;
    @NotNull(message = "Username cannot be null")
    String username;
    String firstName;
    String lastName;
    String email;
    String phone;
    Date birthDate;
    Date hireDate;
    Date fireDate;
    JobTitle jobTitle;
    BigDecimal salary;
    Boolean isHired;
    Integer yearsOfExperience;
    Privilege privilege;
    Integer vacationDays;
}