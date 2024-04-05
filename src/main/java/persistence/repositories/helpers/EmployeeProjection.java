package persistence.repositories.helpers;

import lombok.Data;

@Data
public class EmployeeProjection {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String jobTitle;
    private Double salary;
    private Boolean isHired;

    public static class Builder {
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String jobTitle;
        private Double salary;
        private Boolean isHired;

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder withJobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
            return this;
        }

        public Builder withSalary(Double salary) {
            this.salary = salary;
            return this;
        }

        public Builder withIsHired(Boolean isHired) {
            this.isHired = isHired;
            return this;
        }

        public EmployeeProjection build() {
            EmployeeProjection employeeProjection = new EmployeeProjection();
            employeeProjection.username = this.username;
            employeeProjection.firstName = this.firstName;
            employeeProjection.lastName = this.lastName;
            employeeProjection.email = this.email;
            employeeProjection.phone = this.phone;
            employeeProjection.jobTitle = this.jobTitle;
            employeeProjection.salary = this.salary;
            employeeProjection.isHired = this.isHired;
            return employeeProjection;
        }
    }
}