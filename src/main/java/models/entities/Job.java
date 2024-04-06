package models.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import models.enums.JobTitle;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "jobs")
public class Job extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotEmpty(message = "Name cannot be empty")
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private JobTitle title;

    private String description;

    @Column(name = "starting_salary")
    @PositiveOrZero(message = "Starting salary should be positive or zero")
    private BigDecimal startingSalary;

    @Column(name = "max_salary")
    @PositiveOrZero(message = "Max salary should be positive or zero")
    private BigDecimal maxSalary;

    @Column(name = "min_experience")
    @PositiveOrZero(message = "Min experience should be positive or zero")
    private Integer minExperience;

    @OneToMany(mappedBy = "job")
    private Set<Employee> employees = new HashSet<>();


}
