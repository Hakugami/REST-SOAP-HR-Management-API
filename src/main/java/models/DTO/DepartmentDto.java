package models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import models.entities.Department;

import java.io.Serializable;

/**
 * DTO for {@link Department}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DepartmentDto extends BaseDTO implements Serializable {
    Long id;
    String name;
    String managerName;
    String description;
    Boolean active;
}