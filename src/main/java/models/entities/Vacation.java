package models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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
    @Column(name = "start_date" , nullable = false)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date" , nullable = false)
    private Date endDate;

    @Column(name = "reason", nullable = false)
    @NotEmpty(message = "Reason cannot be empty")
    private String reason;

    @Column(name = "is_approved" , nullable = false)
    private Boolean isApproved;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public void setEmployee(Employee employee) {
        this.employee = employee;
        employee.getVacations().add(this);
    }

}
