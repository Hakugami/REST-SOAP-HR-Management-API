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

    @JsonbTransient
    public void setId(Long id) {
        this.id = id;
    }

    @JsonbTransient
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
    @JsonbTransient
    public void setFireDate(LocalDate fireDate) {
        this.fireDate = fireDate;
    }
    @JsonbTransient
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
    @JsonbTransient
    public void setHired(Boolean hired) {
        isHired = hired;
    }
    @JsonbTransient
    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }
    @JsonbTransient
    public void setVacationDays(Integer vacationDays) {
        this.vacationDays = vacationDays;
    }
    @JsonbTransient
    public void setDeduction(Double deduction) {
        this.deduction = deduction;
    }

    @JsonbTransient
    public String getPassword() {
        return password;
    }
}
