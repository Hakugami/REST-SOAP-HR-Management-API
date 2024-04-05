package models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "vacations")
public class Vacation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    private String reason;

    private Boolean isApproved;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
