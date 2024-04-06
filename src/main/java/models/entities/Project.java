package models.entities;


import jakarta.persistence.*;
import lombok.*;
import models.enums.ProjectStatus;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "projects")
public class Project extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private String description;

    @Enumerated(EnumType.ORDINAL)
    private ProjectStatus status;

    private Integer durationInMonths;

    private Integer teamSize;

    private String clientName;

    @OneToMany(mappedBy = "project")
    private Set<Employee> employees;
}
