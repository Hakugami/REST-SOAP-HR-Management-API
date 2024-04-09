package controllers.rest.beans;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorMessage {
    private String message;
    private int errorCode;
    private String documentation;
}
