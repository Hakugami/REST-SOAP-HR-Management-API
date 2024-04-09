package models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "departments")
public class Department extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private String description;

    private Boolean active;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Employee> employees = new HashSet<>();

    @Transient
    private String managerName;

    @PostLoad
    private void postLoad() {
        if (manager != null) {
            managerName = manager.getFirstName() + " " + manager.getLastName();
        }
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        employee.setDepartment(this);
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
        employee.setDepartment(null);
    }

    public void setManager(Manager manager) {
        this.manager = manager;
        manager.setDepartment(this);
    }

    public void removeManager() {
        if (manager != null) {
            manager.setDepartment(null);
            manager = null;
        }
    }

    public void clearEmployees() {
        for (Employee employee : employees) {
            employee.setDepartment(null);
        }
        employees.clear();
    }

    public void clearManager() {
        if (manager != null) {
            manager.setDepartment(null);
            manager = null;
        }
    }

    public void clear() {
        clearEmployees();
        clearManager();
    }


}
