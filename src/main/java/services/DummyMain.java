package services;

import lombok.extern.slf4j.Slf4j;
import models.entities.Department;
import persistence.manager.DatabaseSingleton;

@Slf4j
public class DummyMain {
    public static void main(String[] args) {
//        List<Job> jobs = new ArrayList<>();
//
//        for (JobTitle title : JobTitle.values()) {
//            Job job = new Job();
//            job.setTitle(title);
//            job.setStartingSalary(BigDecimal.valueOf(title.getMinSalary()));
//            job.setMaxSalary(BigDecimal.valueOf(title.getMaxSalary()));
//            job.setMinExperience(title.getRequiredExperience());
//            job.setDescription(title.getDescription());
//            jobs.add(job);
//            job.setAvailable(true);
//        }
//
//        DatabaseSingleton.INSTANCE.doInTransaction(entityManager -> {
//            JobRepository jobRepository = JobRepository.getInstance();
//            jobs.forEach(job -> jobRepository.create(job, entityManager));
//        });

//        DatabaseSingleton.INSTANCE.doInTransaction(entityManager -> {
//            JobRepository jobRepository = JobRepository.getInstance();
//            List<Job> jobs = jobRepository.readAll(entityManager, 0, 10, null);
//            jobs.forEach(job ->{
//                job.setAvailable(true);
//                jobRepository.update(job, entityManager);
//            } );
//        });

        Department department = new Department();
        department.setName("HR");
        department.setActive(true);
        department.setDescription("Human Resources Department");

        Department department2 = new Department();
        department2.setName("IT");
        department2.setActive(true);
        department2.setDescription("Information Technology Department");

        Department department3 = new Department();
        department3.setName("Finance");
        department3.setActive(true);
        department3.setDescription("Finance Department");

        DatabaseSingleton.INSTANCE.doInTransaction(entityManager -> {
            entityManager.persist(department);
        });

    }

}
