package persistence.manager.helpers;

import jakarta.persistence.EntityManager;
import persistence.manager.DatabaseSingleton;


public record AutoCloseableEntityManager(EntityManager entityManager) implements AutoCloseable {

    @Override
    public void close() {
        DatabaseSingleton.INSTANCE.closeEntityManager();
    }
}
