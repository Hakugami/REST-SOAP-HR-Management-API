package persistence.repositories.helpers.filters;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.ws.rs.QueryParam;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public abstract class AbstractFilter<T> {
    @QueryParam("name")
    protected String name;
    @QueryParam("sortOrder")
    protected String sortOrder;
    @QueryParam("sortBy")
    protected String sortBy;

    protected AbstractFilter(String name, String sortOrder, String sortBy) {
        this.name = name;
        this.sortOrder = sortOrder;
        this.sortBy = sortBy;
    }

    public abstract Predicate toPredicateConjunction(CriteriaBuilder criteriaBuilder, Root<T> root);

    public abstract Predicate toPredicateDisjunction(CriteriaBuilder criteriaBuilder, Root<T> root);

    public abstract Order toOrder(CriteriaBuilder criteriaBuilder, Root<T> root);

}
