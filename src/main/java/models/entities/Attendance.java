package models.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import models.enums.AttendanceStatus;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "attendances")
public class Attendance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull(message = "Start Date cannot be null")
    @Column(name = "AttendanceTime", nullable = false)
    private Date attendanceTime;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Enumerated(EnumType.ORDINAL)
    @NotNull(message = "Status cannot be null")
    private AttendanceStatus status;

    @Transient
    private String employeeName;

    @PostLoad
    private void postLoad() {
        if (employee != null) {
            employeeName = employee.getFirstName() + " " + employee.getLastName();
        }
    }
}
