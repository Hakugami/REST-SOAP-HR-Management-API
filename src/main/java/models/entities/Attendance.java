package models.entities;


import jakarta.persistence.*;
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
    private Date date;

    @ManyToOne
    private Employee employee;

    @Enumerated(EnumType.ORDINAL)
    private AttendanceStatus status;

}
