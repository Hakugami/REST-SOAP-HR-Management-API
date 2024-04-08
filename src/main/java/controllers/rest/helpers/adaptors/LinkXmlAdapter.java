package controllers.rest.helpers.adaptors;

import jakarta.ws.rs.core.Link;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class LinkXmlAdapter extends XmlAdapter<LinkXmlAdapter.AdaptedLink, Link> {

    @Override
    public Link unmarshal(AdaptedLink adaptedLink) {
        // Not needed for our scenario
        return null;
    }

    @Override
    public AdaptedLink marshal(Link link) {
        return new AdaptedLink(link);
    }

    @XmlRootElement(name = "link")
    static class AdaptedLink {
        @XmlAttribute
        public String rel;
        @XmlAttribute
        public String href;

        public AdaptedLink() {
        }

        public AdaptedLink(Link link) {
            this.rel = link.getRel();
            this.href = link.getUri().toString();
        }
    }
}
