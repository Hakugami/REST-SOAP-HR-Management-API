package controllers.rest.helpers.utils;

import controllers.rest.beans.PaginationBean;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RestUtil {
    private RestUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static URI getCreatedAtUriForPost(UriInfo uriInfo, Long id) {
        return uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build();
    }

    public static Link createAbsoluteLinkSelf(UriInfo uriInfo) {
        return Link.fromUriBuilder(uriInfo.getAbsolutePathBuilder()).rel("self").build();
    }

    public static List<Link> createPaginatedResourceLink(UriInfo uriInfo, PaginationBean paginationBean, Long total) {
        Link self = createAbsoluteLinkSelf(uriInfo);
        Optional<Link> next = createNextLink(uriInfo, paginationBean, total);
        Optional<Link> prev = createPrevLink(uriInfo, paginationBean);
        List<Link> links = new ArrayList<>();
        links.add(self);
        next.ifPresent(links::add);
        prev.ifPresent(links::add);
        return links;
    }

    public static Optional<Link> createNextLink(UriInfo uriInfo, PaginationBean paginationBean, Long total) {
        int nextOffset = paginationBean.getOffset() + paginationBean.getLimit();
        if (nextOffset >= total) {
            return Optional.empty();
        }
        return Optional.of(Link.fromUriBuilder(uriInfo
                        .getAbsolutePathBuilder()
                        .queryParam("limit", paginationBean.getLimit())
                        .queryParam("offset", nextOffset))
                .rel("next").build());
    }

    public static Optional<Link> createPrevLink(UriInfo uriInfo, PaginationBean paginationBean) {
        int prevOffset = paginationBean.getOffset() - paginationBean.getLimit();
        if (prevOffset < 0) {
            return Optional.empty();
        }
        return Optional.of(Link.fromUriBuilder(uriInfo
                        .getAbsolutePathBuilder()
                        .queryParam("limit", paginationBean.getLimit())
                        .queryParam("offset", prevOffset))
                .rel("prev").build());
    }

    public static <T> Link createSelfLinkForResponseNestedCollection(UriInfo uriInfo, Long id, Class<T> resourceClass) {
        return Link.fromUriBuilder(uriInfo.getBaseUriBuilder()
                        .path(resourceClass)
                        .path(String.valueOf(id)))
                .rel("self")
                .build();
    }

    public static <T> Link createSelfLink( UriInfo uriInfo, Long id, Class<T> resourceClass ) {
        return Link.fromUriBuilder( uriInfo.getBaseUriBuilder()
                        .path( resourceClass )
                        .path( String.valueOf( id ) ) )
                .rel( "self" )
                .build();
    }

    public static Link createSelfLinkForResponseWithoutIdPathParam( UriInfo uriInfo, Long id ) {
        return Link.fromUriBuilder( uriInfo.getAbsolutePathBuilder().path( String.valueOf( id ) ) ).rel( "self" ).build();
    }
}
