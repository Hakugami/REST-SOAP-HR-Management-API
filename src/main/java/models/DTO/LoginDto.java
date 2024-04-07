package models.DTO;

import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XmlRootElement
@JsonbNillable
public class LoginDto extends BaseDTO{
    private String username;
    private String password;
}
