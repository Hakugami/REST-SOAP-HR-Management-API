package persistence.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import lombok.extern.slf4j.Slf4j;
import models.entities.Employee;
import persistence.repositories.GenericRepository;
import persistence.repositories.helpers.EmployeeProjection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class EmployeeRepository extends GenericRepository<Employee, Long> {
    protected EmployeeRepository() {
        super(Employee.class);
    }

    private static class SingletonHelper {
        private static final EmployeeRepository INSTANCE = new EmployeeRepository();
    }
    public static EmployeeRepository getInstance() {
        return EmployeeRepository.SingletonHelper.INSTANCE;
    }

    public Optional<EmployeeProjection> employeePartialResponse(String[] fields, Long id, EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Employee> root = criteriaQuery.from(Employee.class);
        List<Selection<?>> selections = new ArrayList<>();

        for (String field : fields) {
            switch (field) {
                case "id":
                    selections.add(root.get("id"));
                    break;
                case "username":
                    selections.add(root.get("username"));
                    break;
                case "firstName":
                    selections.add(root.get("firstName"));
                    break;
                case "lastName":
                    selections.add(root.get("lastName"));
                    break;
                case "email":
                    selections.add(root.get("email"));
                    break;
                case "phone":
                    selections.add(root.get("phone"));
                    break;
                case "jobTitle":
                    selections.add(root.get("jobTitle"));
                    break;
                case "salary":
                    selections.add(root.get("salary"));
                    break;
                case "isHired":
                    selections.add(root.get("isHired"));
                    break;
                case "address":
                    selections.add(root.get("address"));
                    break;
                case "manager":
                    selections.add(root.get("manager"));
                    break;
                case "managedEmployees":
                    selections.add(root.get("managedEmployees"));
                    break;
                case "vacations":
                    selections.add(root.get("vacations"));
                    break;
                case "department":
                    selections.add(root.get("department"));
                    break;
                default:
                    log.error("Invalid field: " + field);
            }
        }
        criteriaQuery.multiselect(selections);

        List<Object[]> results = entityManager.createQuery(criteriaQuery).getResultList();
        List<EmployeeProjection> projections = new ArrayList<>();

        for (Object[] result : results) {
            EmployeeProjection projection = new EmployeeProjection.Builder()
                    .withUsername(result[0] != null ? (String) result[0] : null)
                    .withFirstName(result[1] != null ? (String) result[1] : null)
                    .withLastName(result[2] != null ? (String) result[2] : null)
                    .withEmail(result[3] != null ? (String) result[3] : null)
                    .withPhone(result[4] != null ? (String) result[4] : null)
                    .withJobTitle(result[5] != null ? (String) result[5] : null)
                    .withSalary(result[6] != null ? (Double) result[6] : null)
                    .build();
            projections.add(projection);
        }

        return Optional.of(projections.getFirst());
    }
}