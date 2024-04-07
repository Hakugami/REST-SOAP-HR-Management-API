package persistence.repositories.impl;

import lombok.extern.slf4j.Slf4j;
import models.entities.Job;
import persistence.repositories.GenericRepository;


@Slf4j
public class JobRepository extends GenericRepository<Job, Long> {

    protected JobRepository() {
        super(Job.class);
    }

    public static JobRepository getInstance() {
        return SingletonHelper.INSTANCE;
    }



    private static class SingletonHelper {
        private static final JobRepository INSTANCE = new JobRepository();
    }


}
