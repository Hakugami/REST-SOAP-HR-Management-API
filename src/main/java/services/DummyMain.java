package services;

import lombok.extern.slf4j.Slf4j;
import models.entities.Job;
import models.enums.JobTitle;
import persistence.manager.DatabaseSingleton;
import persistence.repositories.impl.JobRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DummyMain {
    public static void main(String[] args) {
        List<Job> jobs = new ArrayList<>();

        for (JobTitle title : JobTitle.values()) {
            Job job = new Job();
            job.setTitle(title);
            job.setStartingSalary(BigDecimal.valueOf(title.getMinSalary()));
            job.setMaxSalary(BigDecimal.valueOf(title.getMaxSalary()));
            job.setMinExperience(title.getRequiredExperience());
            job.setDescription(title.getDescription());
            jobs.add(job);
        }


    }

}
