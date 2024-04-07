package models.DTO;

import controllers.rest.helpers.LocalDateDeserializer;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.json.bind.annotation.JsonbTypeDeserializer;
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
@JsonbNillable
public class EmployeeDto extends BaseDTO implements Serializable {
    Long id;
    String username;
    String password;
    String firstName;
    String lastName;
    String email;
    String phone;

    @JsonbDateFormat("yyyy-MM-dd")
    @JsonbTypeDeserializer(LocalDateDeserializer.class)
    LocalDate birthDate;

    @JsonbDateFormat("yyyy-MM-dd")
    @JsonbTypeDeserializer(LocalDateDeserializer.class)
    LocalDate hireDate;

    @JsonbDateFormat("yyyy-MM-dd")
    @JsonbTypeDeserializer(LocalDateDeserializer.class)
    LocalDate fireDate;

    BigDecimal salary;
    Boolean isHired;
    Integer yearsOfExperience;
    Privilege privilege;
    Integer vacationDays;
    JobTitle jobTitle;
    Double deduction;

}
