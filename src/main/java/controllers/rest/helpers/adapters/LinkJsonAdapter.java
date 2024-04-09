package controllers.rest.helpers.adapters;

import controllers.rest.beans.AdaptedLink;
import jakarta.json.bind.adapter.JsonbAdapter;
import jakarta.ws.rs.core.Link;

import java.util.List;

public class LinkJsonAdapter implements JsonbAdapter<List<Link>, List<AdaptedLink>> {
    @Override
    public List<AdaptedLink> adaptToJson(List<Link> obj) throws Exception {
        return obj.stream()
                .map(link -> new AdaptedLink(link.getUri(), link.getRel()))
                .toList();
    }

    @Override
    public List<Link> adaptFromJson(List<AdaptedLink> obj) throws Exception {
        return obj.stream()
                .map(link -> Link.fromUri(link.getUri()).rel(link.getRel()).build())
                .toList();
    }
}
