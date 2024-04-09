package models.DTO;

import controllers.rest.helpers.adaptors.LocalDateDeserializer;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.json.bind.annotation.JsonbTypeDeserializer;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

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

    @Size(min = 3, message = "Username should be at least 3 characters")
    @NotEmpty(message = "Username cannot be empty")
    String username;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 6, message = "Password should be at least 6 characters")
    String password;

    String firstName;
    String lastName;

    @Email(message = "Email should be valid")
    String email;

    @Digits(integer = 11, fraction = 0, message = "Phone should be valid")
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
    @NotNull(message = "Years of experience cannot be null")
    Integer yearsOfExperience;

    Integer vacationDays;
    Double deduction;

    AddressDto address;

}
