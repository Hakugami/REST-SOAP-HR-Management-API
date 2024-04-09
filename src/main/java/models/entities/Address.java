package models.entities;


import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address extends BaseEntity {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
