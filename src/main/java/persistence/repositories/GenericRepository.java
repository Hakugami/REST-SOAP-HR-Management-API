package persistence.repositories;

import models.entities.BaseEntity;

public abstract class GenericRepository<T extends BaseEntity,ID > {
    private final Class<T> entityClass;

    protected GenericRepository(Class<T> entityClass){
        this.entityClass = entityClass;
    }
}
