package controllers.rest.resources.employee;

import controllers.rest.helpers.adaptors.LinkJsonAdapter;
import controllers.rest.helpers.adaptors.LinkXmlAdapter;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.json.bind.annotation.JsonbTypeAdapter;
import jakarta.ws.rs.core.Link;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import persistence.repositories.helpers.projections.EmployeeProjection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse implements Serializable {

    private EmployeeProjection employee;

    @XmlElementWrapper(name = "links")
    @XmlElement(name = "link")
    @XmlJavaTypeAdapter(LinkXmlAdapter.class) // Apply the adapter for XML serialization
    @JsonbTransient
    private List<Link> xmlLinks = new ArrayList<>();

    @JsonbTypeAdapter(LinkJsonAdapter.class)
    private List<Link> links = new ArrayList<>(); // Field for JSON representation

    public void addLink(Link link) {
        this.xmlLinks.add(link);
        this.links.add(link);
    }
}
