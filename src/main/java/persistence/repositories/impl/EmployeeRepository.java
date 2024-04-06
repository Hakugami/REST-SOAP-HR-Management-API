package persistence.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import lombok.extern.slf4j.Slf4j;
import models.entities.Employee;
import persistence.repositories.GenericRepository;
import persistence.repositories.helpers.EmployeeProjection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

@Slf4j
public class EmployeeRepository extends GenericRepository<Employee, Long> {
    protected EmployeeRepository() {
        super(Employee.class);
    }

    public static EmployeeRepository getInstance() {
        return EmployeeRepository.SingletonHelper.INSTANCE;
    }

    public Optional<EmployeeProjection> employeePartialResponse(Long id, EntityManager entityManager, Set<String> fields) {
        try {
            if(fields.isEmpty()) {
                fields = getAllFieldNames();
            }
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = cb.createTupleQuery();
            Root<Employee> root = query.from(Employee.class);
            List<Selection<?>> selections = new ArrayList<>();
            fields.forEach(field -> selections.add(root.get(field).alias(field)));
            query.multiselect(selections.toArray(new Selection[0]));
            query.where(cb.equal(root.get("id"), id));
            Tuple result = entityManager.createQuery(query).getSingleResult();

            return Optional.ofNullable(buildEmployeeProjection(result, fields));
        } catch (Exception e) {
            log.error("An error occurred during employeePartialResponse operation: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<EmployeeProjection> getAllEmployeesPartialResponse(EntityManager entityManager, Set<String> fields, int page, int pageSize) {
        try {
            if(fields.isEmpty()) {
                fields = getAllFieldNames();
            }
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = cb.createTupleQuery();
            Root<Employee> root = query.from(Employee.class);
            List<Selection<?>> selections = new ArrayList<>();
            fields.forEach(field -> selections.add(root.get(field).alias(field)));
            query.multiselect(selections.toArray(new Selection[0]));

            // Pagination
            int offset = page * pageSize;
            TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
            typedQuery.setFirstResult(offset);
            typedQuery.setMaxResults(pageSize);

            List<Tuple> results = typedQuery.getResultList();
            List<EmployeeProjection> projections = new ArrayList<>();

            for (Tuple result : results) {
                projections.add(buildEmployeeProjection(result, fields));
            }

            return projections;
        } catch (Exception e) {
            log.error("An error occurred during getAllEmployeesPartialResponse operation: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private Set<String> getAllFieldNames() {
        Set<String> fieldNames = new HashSet<>();
        Field[] fields = EmployeeProjection.class.getDeclaredFields();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                fieldNames.add(field.getName());
            }
        }
        return fieldNames;
    }
    private EmployeeProjection buildEmployeeProjection(Tuple result, Set<String> fields) {
        EmployeeProjection.Builder builder = new EmployeeProjection.Builder();
        fields.forEach(field -> {
            if (result.get(field) != null) {
                try {
                    Method method = EmployeeProjection.Builder.class.getMethod(field, result.get(field).getClass());
                    method.invoke(builder, result.get(field));
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    log.error("An error occurred during employeePartialResponse operation: " + e.getMessage());
                }
            }
        });
        return builder.build();
    }

    private static class SingletonHelper {
        private static final EmployeeRepository INSTANCE = new EmployeeRepository();
    }
}
