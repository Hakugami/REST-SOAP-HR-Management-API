package persistence.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import models.entities.BaseEntity;
import persistence.repositories.helpers.filters.AbstractFilter;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class GenericRepository<T extends BaseEntity, ID> {
    private final Class<T> entityClass;

    protected GenericRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public boolean create(T t, EntityManager entityManager) {
        try {
            entityManager.persist(t);
            return true;
        } catch (Exception e) {
            log.error("An error occurred during create operation: " + e.getMessage());
            return false;
        }
    }

    public T read(ID id, EntityManager entityManager) {
        try {
            return entityManager.find(entityClass, id);
        } catch (Exception e) {
            log.error("An error occurred during read operation: " + e.getMessage());
            return null;
        }
    }

    public boolean update(T t, EntityManager entityManager) {
        try {
            entityManager.merge(t);
            return true;
        } catch (Exception e) {
            log.error("An error occurred during update operation: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(ID id, EntityManager entityManager) {
        try {
            T t = entityManager.find(entityClass, id);
            entityManager.remove(t);
            return true;
        } catch (Exception e) {
            log.error("An error occurred during delete operation: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(T t, EntityManager entityManager) {
        try {
            entityManager.remove(t);
            return true;
        } catch (Exception e) {
            log.error("An error occurred during delete operation: " + e.getMessage());
            return false;
        }
    }

    public List<T> readAll(EntityManager entityManager, int page, int pageSize) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(entityClass);
            Root<T> rootEntry = cq.from(entityClass);
            CriteriaQuery<T> all = cq.select(rootEntry);
            TypedQuery<T> allQuery = entityManager.createQuery(all);
            allQuery.setFirstResult(page * pageSize);
            allQuery.setMaxResults(pageSize);
            return allQuery.getResultList();
        } catch (Exception e) {
            log.error("An error occurred during readAll operation: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public <F extends AbstractFilter<T>> List<T> readAll(EntityManager entityManager, int page, int pageSize, F filter) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(entityClass);
            Root<T> rootEntry = cq.from(entityClass);

            if (filter != null) {
                Predicate predicate = filter.toPredicateConjunction(cb, rootEntry);
                if (predicate != null) {
                    cq.where(predicate);
                }
                Order order = filter.toOrder(cb, rootEntry);
                if (order != null) {
                    cq.orderBy(order);
                }
            }

            CriteriaQuery<T> all = cq.select(rootEntry);
            TypedQuery<T> allQuery = entityManager.createQuery(all);
            allQuery.setFirstResult(page * pageSize);
            allQuery.setMaxResults(pageSize);
            return allQuery.getResultList();
        } catch (Exception e) {
            log.error("An error occurred during readAll operation: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public long count(EntityManager entityManager) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<T> rootEntry = cq.from(entityClass);
            cq.select(cb.count(rootEntry));
            return entityManager.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            log.error("An error occurred during count operation: {}", e.getMessage());
            return 0;
        }
    }

    public long count(EntityManager entityManager, AbstractFilter<T> filter) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<T> rootEntry = cq.from(entityClass);
            cq.select(cb.count(rootEntry));
            if (filter != null) {
                Predicate predicate = filter.toPredicateConjunction(cb, rootEntry);
                if (predicate != null) {
                    cq.where(predicate);
                }
            }
            return entityManager.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            log.error("An error occurred during count operation: {}", e.getMessage());
            return 0;
        }
    }

}
