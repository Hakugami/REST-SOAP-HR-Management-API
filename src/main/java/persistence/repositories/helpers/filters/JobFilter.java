package persistence.repositories.helpers.filters;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.ws.rs.QueryParam;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.entities.Job;

import java.math.BigDecimal;

@Slf4j
@NoArgsConstructor
public class JobFilter extends AbstractFilter<Job> {

    @QueryParam("title")
    protected String title;
    @QueryParam("minSalary")
    protected BigDecimal minSalary;
    @QueryParam("maxSalary")
    protected BigDecimal maxSalary;
    @QueryParam("minExperience")
    protected Integer minExperience;

    public JobFilter(String name, String sortOrder, String sortBy, String title, BigDecimal minSalary, BigDecimal maxSalary, Integer minExperience) {
        super(name, sortOrder, sortBy);
        this.title = title;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.minExperience = minExperience;
    }

    private JobFilter(Builder builder) {
        super(builder.name, builder.sortOrder, builder.sortBy);
        this.title = builder.title;
        this.minSalary = builder.minSalary;
        this.maxSalary = builder.maxSalary;
        this.minExperience = builder.minExperience;
    }

    @Override
    public Predicate toPredicateConjunction(CriteriaBuilder criteriaBuilder, Root<Job> root) {
        Predicate predicate = criteriaBuilder.conjunction();

        if (title != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("title").as(String.class), "%" + title + "%"));
        }
        if (minSalary != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("startingSalary"), minSalary));
        }
        if (maxSalary != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("maxSalary"), maxSalary));
        }
        if (minExperience != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("minExperience"), minExperience));
        }
        if (name != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("description"), "%" + name + "%"));
        }


        return predicate;
    }

    @Override
    public Predicate toPredicateDisjunction(CriteriaBuilder criteriaBuilder, Root<Job> root) {
        return null;
    }

    @Override
    public Order toOrder(CriteriaBuilder criteriaBuilder, Root<Job> root) {
        if (sortBy == null) {
            return criteriaBuilder.asc(root.get("id"));
        }
        if ("desc".equalsIgnoreCase(sortOrder)) {
            log.info("Sorting desc by {}", sortBy);
            return criteriaBuilder.desc(root.get(sortBy));
        } else {
            log.info("Sorting asc by {}", sortBy);
            return criteriaBuilder.asc(root.get(sortBy));
        }
    }

    public static class Builder {
        private String name;
        private String sortOrder;
        private String sortBy;
        private String title;
        private BigDecimal minSalary;
        private BigDecimal maxSalary;
        private Integer minExperience;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder sortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public Builder sortBy(String sortBy) {
            this.sortBy = sortBy;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder minSalary(BigDecimal minSalary) {
            this.minSalary = minSalary;
            return this;
        }

        public Builder maxSalary(BigDecimal maxSalary) {
            this.maxSalary = maxSalary;
            return this;
        }

        public Builder minExperience(Integer minExperience) {
            this.minExperience = minExperience;
            return this;
        }

        public JobFilter build() {
            return new JobFilter(this);
        }
    }
}