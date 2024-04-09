package persistence.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import models.entities.Department;
import models.entities.Employee;
import persistence.repositories.GenericRepository;

import java.util.List;

public class DepartmentRepository extends GenericRepository<Department, Long> {
    protected DepartmentRepository() {
        super(Department.class);
    }


    public static DepartmentRepository getInstance() {
        return SingletonHelper.INSTANCE;
    }


    public Department findByName(String name, EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        Root<Department> root = cb.createQuery(Department.class).from(Department.class);
        return entityManager.createQuery(entityManager.getCriteriaBuilder().createQuery(Department.class)
                .select(root)
                .where(cb.equal(root.get("name"), name))
        ).getSingleResult();
    }

    public List<Employee> getEmployees(Long id, int offset, int limit, EntityManager entityManager) {
        //criteria query
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        query.select(root);
        query.where(cb.equal(root.get("department").get("id"), id));
        TypedQuery<Employee> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(offset * limit);
        typedQuery.setMaxResults(limit);
        return typedQuery.getResultList();
    }

    public Long countEmployees(Long id, EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Employee> root = query.from(Employee.class);
        query.select(cb.count(root));
        query.where(cb.equal(root.get("department").get("id"), id));
        return entityManager.createQuery(query).getSingleResult();
    }

    private static class SingletonHelper {
        public static final DepartmentRepository INSTANCE = new DepartmentRepository();
    }

}
