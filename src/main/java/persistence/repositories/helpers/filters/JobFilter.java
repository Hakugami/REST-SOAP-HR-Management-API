package persistence.repositories.helpers.filters;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import models.entities.Job;
import models.enums.JobTitle;

import java.math.BigDecimal;

public class JobFilter extends AbstractFilter<Job> {

    private final JobTitle title;
    private final BigDecimal minSalary;
    private final BigDecimal maxSalary;
    private final Integer minExperience;

    private JobFilter(Builder builder) {
        super(builder.name, builder.sortOrder, builder.sortBy);
        this.title = builder.title;
        this.minSalary = builder.minSalary;
        this.maxSalary = builder.maxSalary;
        this.minExperience = builder.minExperience;
    }

    public static class Builder {
        private String name;
        private String sortOrder;
        private String sortBy;
        private JobTitle title;
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

        public Builder title(JobTitle title) {
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

    @Override
    public Predicate toPredicateConjunction(CriteriaBuilder criteriaBuilder, Root<Job> root) {
        Predicate predicate = criteriaBuilder.conjunction();

        if (title != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("title"), title));
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

        return predicate;
    }

    @Override
    public Predicate toPredicateDisjunction(CriteriaBuilder criteriaBuilder, Root<Job> root) {
        return null;
    }

    @Override
    public Order toOrder(CriteriaBuilder criteriaBuilder, Root<Job> root) {
        if(sortBy == null) {
            return criteriaBuilder.asc(root.get("id"));
        }
        if ("desc".equalsIgnoreCase(sortOrder)) {
            return criteriaBuilder.desc(root.get(sortBy));
        } else {
            return criteriaBuilder.asc(root.get(sortBy));
        }
    }
}