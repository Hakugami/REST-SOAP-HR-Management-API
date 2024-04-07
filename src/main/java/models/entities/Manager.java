package models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity(name = "managers")
@DiscriminatorValue("Manager")
public class Manager extends Employee {

    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    private Set<Employee> managedEmployees = new HashSet<>();

    @OneToOne(mappedBy = "manager" , optional = false)
    private Department department;

}