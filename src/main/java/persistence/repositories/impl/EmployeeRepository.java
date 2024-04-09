package persistence.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import models.entities.Employee;
import persistence.repositories.GenericRepository;
import persistence.repositories.helpers.projections.EmployeeProjection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

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
            if (fields.isEmpty()) {
                fields = getAllFieldNames();
            }else if (!fields.contains("id")) {
                fields = new CopyOnWriteArraySet<>(fields);
                fields.add("id");
            }
            log.info("Fields: {}", fields);
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
            if (fields.isEmpty()) {
                fields = getAllFieldNames();
            }else if (!fields.contains("id")) {
                fields = new CopyOnWriteArraySet<>(fields);
                fields.add("id");
            }
            log.info("Fields: {}", fields);
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = cb.createTupleQuery();
            Root<Employee> root = query.from(Employee.class);
            List<Selection<?>> selections = new ArrayList<>();
            fields.forEach(field -> selections.add(root.get(field).alias(field)));
            query.multiselect(selections.toArray(new Selection[0]));

            // Pagination
            return getEmployeeProjections(entityManager, fields, page, pageSize, query);
        } catch (Exception e) {
            log.error("An error occurred during getAllEmployeesPartialResponse operation: {}", e.getMessage());
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

    public Optional<Employee> readByEmail(String email, EntityManager entityManager) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
            Root<Employee> root = query.from(Employee.class);
            query.select(root).where(cb.equal(root.get("email"), email));
            return Optional.ofNullable(entityManager.createQuery(query).getSingleResult());
        } catch (Exception e) {
            log.error("An error occurred during readByEmail operation: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Employee> readByUsername(String username, EntityManager entityManager) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
            Root<Employee> root = query.from(Employee.class);
            query.select(root).where(cb.equal(root.get("username"), username));
            return Optional.ofNullable(entityManager.createQuery(query).getSingleResult());
        } catch (Exception e) {
            log.error("An error occurred during readByUsername operation: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Employee> readByPhone(String phone, EntityManager entityManager) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
            Root<Employee> root = query.from(Employee.class);
            query.select(root).where(cb.equal(root.get("phone"), phone));
            return Optional.ofNullable(entityManager.createQuery(query).getSingleResult());
        } catch (Exception e) {
            log.error("An error occurred during readByPhone operation: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public List<EmployeeProjection> getAllManagersPartialResponse(EntityManager entityManager, Set<String> fields, int page, int pageSize) {
        try {
            if (fields.isEmpty()) {
                fields = getAllFieldNames();
            }else if (!fields.contains("id")) {
                fields = new CopyOnWriteArraySet<>(fields);
                fields.add("id");
            }
            log.info("Fields: {}", fields);
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = cb.createTupleQuery();
            Root<Employee> root = query.from(Employee.class);
            List<Selection<?>> selections = new ArrayList<>();
            fields.forEach(field -> selections.add(root.get(field).alias(field)));
            query.multiselect(selections.toArray(new Selection[0]));

            Predicate managerPredicate = cb.isNotNull(root.get("managedDepartment"));
            query.where(managerPredicate);

            // Pagination
            return getEmployeeProjections(entityManager, fields, page, pageSize, query);
        } catch (Exception e) {
            log.error("An error occurred during getAllManagersPartialResponse operation: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public EmployeeProjection getManagerPartialResponse(Long id, EntityManager entityManager, Set<String> fields) {
        try {
            if (fields.isEmpty()) {
                fields = getAllFieldNames();
            }else if (!fields.contains("id")) {
                fields = new CopyOnWriteArraySet<>(fields);
                fields.add("id");
            }
            log.info("Fields: {}", fields);
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = cb.createTupleQuery();
            Root<Employee> root = query.from(Employee.class);
            List<Selection<?>> selections = new ArrayList<>();
            fields.forEach(field -> selections.add(root.get(field).alias(field)));
            query.multiselect(selections.toArray(new Selection[0]));
            Predicate managerPredicate = cb.isNotNull(root.get("managedDepartment"));
            query.where(cb.and(cb.equal(root.get("id"), id), managerPredicate));
            Tuple result = entityManager.createQuery(query).getSingleResult();

            return buildEmployeeProjection(result, fields);
        } catch (Exception e) {
            log.error("An error occurred during getManagerPartialResponse operation: {}", e.getMessage());
            return null;
        }
    }

    private List<EmployeeProjection> getEmployeeProjections(EntityManager entityManager, Set<String> fields, int page, int pageSize, CriteriaQuery<Tuple> query) {
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
    }



    private static class SingletonHelper {
        private static final EmployeeRepository INSTANCE = new EmployeeRepository();
    }
}
