package models.entities;


import jakarta.persistence.*;
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

    @Enumerated(EnumType.ORDINAL)
    private JobTitle title;

    private String description;

    private BigDecimal startingSalary;

    private BigDecimal maxSalary;

    private Integer minExperience;

    @OneToMany(mappedBy = "job")
    private Set<Employee> employees = new HashSet<>();


}
