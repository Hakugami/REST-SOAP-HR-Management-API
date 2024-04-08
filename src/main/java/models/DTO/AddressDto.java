package models.DTO;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import models.entities.Address;

import java.io.Serializable;

/**
 * DTO for {@link Address}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@XmlRootElement
public class AddressDto extends BaseDTO implements Serializable {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}