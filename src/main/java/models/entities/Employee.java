package models.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import models.enums.JobTitle;
import models.enums.Privilege;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "employees")
public class Employee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull(message = "Username cannot be null")
    private String username;

    @NotNull(message = "Password cannot be null")
    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @Temporal(TemporalType.DATE)
    private Date hireDate;

    @Temporal(TemporalType.DATE)
    private Date fireDate;

    private JobTitle jobTitle;

    private BigDecimal salary;

    private Boolean isHired;

    private Integer yearsOfExperience;

    private Privilege privilege;

    private Integer vacationDays;

    private Double deduction;

    @Embedded
    private Address address;

    @ManyToOne
    private Job job;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @OneToMany(mappedBy = "manager")
    private Set<Employee> managedEmployees = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    private Set<Vacation> vacations = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    private Set<Attendance> attendances = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

}
