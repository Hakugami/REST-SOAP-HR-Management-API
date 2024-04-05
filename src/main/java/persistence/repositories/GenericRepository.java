package persistence.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import models.entities.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class GenericRepository<T extends BaseEntity,ID > {
    private final Class<T> entityClass;

    protected GenericRepository(Class<T> entityClass){
        this.entityClass = entityClass;
    }

    public boolean create (T t , EntityManager entityManager){
        try{
            entityManager.persist(t);
            return true;
        }catch(Exception e){
            log.error("An error occurred during create operation: " + e.getMessage());
            return false;
        }
    }

    public T read(ID id, EntityManager entityManager){
        try{
            return entityManager.find(entityClass, id);
        }catch(Exception e){
            log.error("An error occurred during read operation: " + e.getMessage());
            return null;
        }
    }

    public boolean update(T t, EntityManager entityManager){
        try{
            entityManager.merge(t);
            return true;
        }catch(Exception e){
            log.error("An error occurred during update operation: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(ID id, EntityManager entityManager){
        try{
            T t = entityManager.find(entityClass, id);
            entityManager.remove(t);
            return true;
        }catch(Exception e){
            log.error("An error occurred during delete operation: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(T t, EntityManager entityManager){
        try{
            entityManager.remove(t);
            return true;
        }catch(Exception e){
            log.error("An error occurred during delete operation: " + e.getMessage());
            return false;
        }
    }

    public List<T> readAll(EntityManager entityManager){
        try{
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(entityClass);
            Root<T> rootEntry = cq.from(entityClass);
            CriteriaQuery<T> all = cq.select(rootEntry);
            TypedQuery<T> allQuery = entityManager.createQuery(all);
            return allQuery.getResultList();
        }catch(Exception e){
            log.error("An error occurred during readAll operation: " + e.getMessage());
            return new ArrayList<>();
        }
    }


}
