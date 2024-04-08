package controllers.rest.beans;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class AdaptedLink {
    @XmlAttribute
    private URI uri;
    @XmlAttribute
    private String rel;
}
