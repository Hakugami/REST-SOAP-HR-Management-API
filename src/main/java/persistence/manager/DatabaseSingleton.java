package persistence.manager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.spi.PersistenceProvider;
import org.hibernate.jpa.HibernatePersistenceProvider;
import persistence.manager.helpers.AutoCloseableEntityManager;
import utils.PersistenceUnitInfoImpl;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public enum DatabaseSingleton {
    INSTANCE;

    private final ThreadLocal<EntityManager> entityManagerThreadLocal = new ThreadLocal<>();
    private final EntityManagerFactory entityManagerFactory;
    private final PersistenceUnitInfoImpl persistenceUnitInfo;

    private DatabaseSingleton() {
        persistenceUnitInfo = new PersistenceUnitInfoImpl();
        PersistenceProvider persistenceProvider = new HibernatePersistenceProvider();
        entityManagerFactory = persistenceProvider.createContainerEntityManagerFactory(persistenceUnitInfo, persistenceUnitInfo.getProperties());
    }

    public void init() {
        // TODO just to initialize the singleton
    }

    public AutoCloseableEntityManager getAutoClosableEntityManager() {
        EntityManager entityManager = entityManagerThreadLocal.get();
        if (entityManager == null) {
            entityManager = entityManagerFactory.createEntityManager();
            entityManagerThreadLocal.set(entityManager);
        }
        return new AutoCloseableEntityManager(entityManager);
    }

    public void doInTransaction(Consumer<EntityManager> consumer) {
        try (AutoCloseableEntityManager autoCloseableEntityManager = getAutoClosableEntityManager()) {
            EntityManager entityManager = autoCloseableEntityManager.entityManager();
            entityManager.getTransaction().begin();
            consumer.accept(entityManager);
            entityManager.getTransaction().commit();
        }
    }

    public <T> T doInTransactionWithResult(Function<EntityManager, T> function) {
        T result = null;
        try (AutoCloseableEntityManager autoCloseableEntityManager = getAutoClosableEntityManager()) {
            EntityManager entityManager = autoCloseableEntityManager.entityManager();
            entityManager.getTransaction().begin();
            result = function.apply(entityManager);
            entityManager.getTransaction().commit();
        }
        return result;
    }

    public void closeEntityManager() {
        EntityManager entityManager = entityManagerThreadLocal.get();
        if (entityManager != null) {
            entityManager.close();
            entityManagerThreadLocal.remove();
        }
    }

    public void closeEntityManagerFactory() {
        entityManagerFactory.close();
    }

}
