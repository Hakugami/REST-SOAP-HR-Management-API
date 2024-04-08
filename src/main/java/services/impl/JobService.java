package services.impl;

import mappers.JobMapper;
import models.DTO.JobDto;
import models.entities.Job;
import models.enums.JobTitle;
import persistence.manager.DatabaseSingleton;
import persistence.repositories.impl.JobRepository;
import services.BaseService;

public class JobService extends BaseService<Job, JobDto, Long> {
    protected JobService() {
        super(JobRepository.getInstance(), JobMapper.INSTANCE);
    }

    public static JobService getInstance() {
        return SingletonHelper.INSTANCE;
    }


    public JobDto getJobByTitle(JobTitle title) {
        return DatabaseSingleton.INSTANCE.doInTransactionWithResult(entityManager -> {
            Job job = JobRepository.getInstance().getJobByTitle(title, entityManager).orElse(null);
            return job == null ? null : JobMapper.INSTANCE.toDTO(job);
        });
    }

    private static class SingletonHelper {
        private static final JobService INSTANCE = new JobService();
    }
}
