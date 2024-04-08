package models.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "employee_type")
public class Employee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotEmpty(message = "Username cannot be empty")
    @Column(nullable = false)
    @Size(min = 3, message = "Username should be at least 3 characters")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    @Column(nullable = false)
    @Size(min = 6, message = "Password should be at least 6 characters")
    private String password;

    @NotEmpty(message = "First name cannot be empty")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    @Column(name ="email",nullable = false, unique = true)
    private String email;

    @NotEmpty(message = "Phone cannot be empty")
    @Digits(integer = 11, fraction = 0, message = "Phone should be valid")
    @Column(name ="phone",nullable = false, unique = true)
    private String phone;

    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    @Past(message = "Birth date should be in the past")
    private Date birthDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "hire_date")
    @PastOrPresent(message = "Hire date should be in the past or present")
    private Date hireDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "fire_date")
    @Past(message = "Fire date should be in the past")
    private Date fireDate;

    @Column(name = "salary")
    private BigDecimal salary;

    @Column(name = "is_hired")
    private Boolean isHired;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "privilege")
    private Privilege privilege;

    @Column(name = "vacation_days")
    private Integer vacationDays;

    @Column(name = "deduction")
    private Double deduction;

    @Column(name ="salt")
    private String salt;

    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @OneToMany(mappedBy = "employee" , fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Vacation> vacations = new HashSet<>();

    @OneToMany(mappedBy = "employee" , fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Attendance> attendances = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")
    private Project project;

}
