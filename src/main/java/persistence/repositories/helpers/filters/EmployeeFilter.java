package persistence.repositories.helpers.filters;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.ws.rs.QueryParam;
import lombok.extern.slf4j.Slf4j;
import models.entities.Employee;
import models.enums.Privilege;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
public class EmployeeFilter extends AbstractFilter<Employee> {
    @QueryParam("username")
    protected String username;
    @QueryParam("firstName")
    protected String firstName;
    @QueryParam("lastName")
    protected String lastName;
    @QueryParam("email")
    protected String email;
    @QueryParam("minSalary")
    protected BigDecimal minSalary;
    @QueryParam("maxSalary")
    protected BigDecimal maxSalary;
    @QueryParam("minExperience")
    protected Integer minExperience;
    @QueryParam("minVacationDays")
    protected Integer minVacationDays;
    @QueryParam("privilege")
    protected Privilege privilege;
    @QueryParam("hiredAfter")
    protected Date hiredAfter;
    @QueryParam("hiredBefore")
    protected Date hiredBefore;
    @QueryParam("phone")
    protected String phone;

    private EmployeeFilter(Builder builder) {
        super(builder.name, builder.sortOrder, builder.sortBy);
        this.username = builder.username;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.minSalary = builder.minSalary;
        this.maxSalary = builder.maxSalary;
        this.minExperience = builder.minExperience;
        this.minVacationDays = builder.minVacationDays;
        this.privilege = builder.privilege;
        this.hiredAfter = builder.hiredAfter;
        this.hiredBefore = builder.hiredBefore;
        this.phone = builder.phone;
    }

    @Override
    public Predicate toPredicateConjunction(CriteriaBuilder criteriaBuilder, Root<Employee> root) {
        Predicate predicate = criteriaBuilder.conjunction();

        if (username != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("username").as(String.class), "%" + username + "%"));
        }
        if (firstName != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("firstName").as(String.class), "%" + firstName + "%"));
        }
        if (lastName != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("lastName").as(String.class), "%" + lastName + "%"));
        }
        if (email != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("email"), email));
        }
        if (minSalary != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("salary"), minSalary));
        }
        if (maxSalary != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("salary"), maxSalary));
        }
        if (minExperience != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("yearsOfExperience"), minExperience));
        }
        if (minVacationDays != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("vacationDays"), minVacationDays));
        }
        if (privilege != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("privilege"), privilege));
        }
        if (hiredAfter != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("hireDate"), hiredAfter));
        }
        if (hiredBefore != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("hireDate"), hiredBefore));
        }

        if (phone != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("phone"), phone));
        }

        return predicate;
    }

    @Override
    public Predicate toPredicateDisjunction(CriteriaBuilder criteriaBuilder, Root<Employee> root) {
        return null;
    }


    @Override
    public Order toOrder(CriteriaBuilder criteriaBuilder, Root<Employee> root) {
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
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private BigDecimal minSalary;
        private BigDecimal maxSalary;
        private Integer minExperience;
        private Privilege privilege;
        private Integer minVacationDays;
        private Date hiredAfter;
        private Date hiredBefore;


        public Builder sortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public Builder sortBy(String sortBy) {
            this.sortBy = sortBy;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
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

        public Builder privilege(Privilege privilege) {
            this.privilege = privilege;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder minVacationDays(Integer minVacationDays) {
            this.minVacationDays = minVacationDays;
            return this;
        }

        public Builder hiredAfter(Date hiredAfter) {
            this.hiredAfter = hiredAfter;
            return this;
        }

        public Builder hiredBefore(Date hiredBefore) {
            this.hiredBefore = hiredBefore;
            return this;
        }

        public EmployeeFilter build() {
            return new EmployeeFilter(this);
        }
    }
}