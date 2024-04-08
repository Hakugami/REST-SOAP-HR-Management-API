package persistence.repositories.helpers.filters;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;



public abstract class AbstractFilter<T> {
    protected String name;
    protected String sortOrder;
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
