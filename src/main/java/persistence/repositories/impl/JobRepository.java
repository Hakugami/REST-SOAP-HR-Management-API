package persistence.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import models.entities.Job;
import models.enums.JobTitle;
import persistence.repositories.GenericRepository;

import java.util.Optional;


@Slf4j
public class JobRepository extends GenericRepository<Job, Long> {

    protected JobRepository() {
        super(Job.class);
    }

    public static JobRepository getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public Optional<Job> getJobByTitle(JobTitle title, EntityManager entityManager) {
        try {
            log.info("Getting job by title: {}", title);
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Job> query = cb.createQuery(Job.class);
            Root<Job> root = query.from(Job.class);
            query.select(root).where(cb.equal(root.get("title"), title));
            return Optional.ofNullable(entityManager.createQuery(query).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            log.error("An error occurred during getJobByTitle operation: {}", e.getMessage());
            return Optional.empty();
        }
    }


    private static class SingletonHelper {
        private static final JobRepository INSTANCE = new JobRepository();
    }


}
