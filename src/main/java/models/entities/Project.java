package models.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import models.enums.ProjectStatus;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "projects")
public class Project extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    private String description;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private ProjectStatus status;

    @Column(name = "duration_in_months")
    @PositiveOrZero(message = "Duration should be positive or zero")
    private Integer durationInMonths;

    @Column(name = "team_size")
    @PositiveOrZero(message = "Team size should be positive or zero")
    private Integer teamSize;

    @Column(name = "client_name")
    @NotEmpty(message = "Client name cannot be empty")
    private String clientName;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Employee> employees;

    public void addEmployee(Employee employee) {
        employees.add(employee);
        employee.setProject(this);
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
        employee.setProject(null);
    }

    public void clearEmployees() {
        employees.forEach(employee -> employee.setProject(null));
        employees.clear();
    }
}
