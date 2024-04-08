package persistence.repositories.helpers.projections;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.ToString;
import mappers.JobMapper;
import models.DTO.JobDto;
import models.entities.Job;

import java.math.BigDecimal;

@Data
@ToString
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EmployeeProjection {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private JobDto job;
    private BigDecimal salary;
    private Boolean isHired;
    private String birthDate;
    private String hireDate;
    private String fireDate;

    public static class Builder {
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private JobDto job;
        private BigDecimal salary;
        private Boolean isHired;
        private String birthDate;

        private String hireDate;

        private String fireDate;

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder job(Job job) {
            this.job = JobMapper.INSTANCE.toDTO(job);
            return this;
        }

        public Builder salary(BigDecimal salary) {
            this.salary = salary;
            return this;
        }

        public Builder isHired(Boolean isHired) {
            this.isHired = isHired;
            return this;
        }

        public Builder birthDate(java.sql.Date birthDate) {
            this.birthDate = birthDate != null ? birthDate.toString() : null;
            return this;
        }

        public Builder hireDate(java.sql.Date hireDate) {
            this.hireDate = hireDate != null ? hireDate.toString() : null;
            return this;
        }

        public Builder fireDate(java.sql.Date fireDate) {
            this.fireDate = fireDate != null ? fireDate.toString() : null;
            return this;
        }

        public EmployeeProjection build() {
            EmployeeProjection employeeProjection = new EmployeeProjection();
            employeeProjection.username = this.username;
            employeeProjection.firstName = this.firstName;
            employeeProjection.lastName = this.lastName;
            employeeProjection.email = this.email;
            employeeProjection.phone = this.phone;
            employeeProjection.job = this.job;
            employeeProjection.salary = this.salary;
            employeeProjection.isHired = this.isHired;
            employeeProjection.birthDate = this.birthDate;
            employeeProjection.hireDate = this.hireDate;
            employeeProjection.fireDate = this.fireDate;
            return employeeProjection;
        }
    }
}