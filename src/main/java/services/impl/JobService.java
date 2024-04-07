package services.impl;

import mappers.JobMapper;
import models.DTO.JobDto;
import models.entities.Job;
import persistence.repositories.impl.JobRepository;
import services.BaseService;

public class JobService extends BaseService<Job, JobDto, Long> {
    protected JobService() {
        super(JobRepository.getInstance(), JobMapper.INSTANCE);
    }

    public static JobService getInstance() {
        return SingletonHelper.INSTANCE;
    }



    private static class SingletonHelper {
        private static final JobService INSTANCE = new JobService();
    }
}
