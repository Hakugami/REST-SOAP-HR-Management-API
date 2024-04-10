package models.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import models.entities.Project;
import models.enums.ProjectStatus;

import java.io.Serializable;

/**
 * DTO for {@link Project}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ProjectDto extends BaseDTO implements Serializable {
    private Long id;
    @NotEmpty(message = "Name cannot be empty")
    private String name;
    private String description;
    private String status;
    @PositiveOrZero(message = "Duration should be positive or zero")
    private Integer durationInMonths;
    @PositiveOrZero(message = "Team size should be positive or zero")
    private Integer teamSize;
    @NotEmpty(message = "Client name cannot be empty")
    private String clientName;
}