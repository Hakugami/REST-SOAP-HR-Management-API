package services;

import lombok.extern.slf4j.Slf4j;
import models.entities.Job;
import models.entities.Project;
import models.enums.JobTitle;
import models.enums.ProjectStatus;
import persistence.manager.DatabaseSingleton;
import persistence.repositories.impl.JobRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
//
//        Department department = new Department();
//        department.setName("HR");
//        department.setActive(true);
//        department.setDescription("Human Resources Department");
//
//        Department department2 = new Department();
//        department2.setName("IT");
//        department2.setActive(true);
//        department2.setDescription("Information Technology Department");
//
//        Department department3 = new Department();
//        department3.setName("Finance");
//        department3.setActive(true);
//        department3.setDescription("Finance Department");
//
//        DatabaseSingleton.INSTANCE.doInTransaction(entityManager -> {
//            DepartmentRepository departmentRepository = DepartmentRepository.getInstance();
//            departmentRepository.create(department, entityManager);
//            departmentRepository.create(department2, entityManager);
//            departmentRepository.create(department3, entityManager);
//        });

        Project project = new Project();
        project.setName("Project 1");
        project.setDescription("Project 1 Description");
        project.setClientName("Client 1");
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setDurationInMonths(12);
        project.setTeamSize(10);

        DatabaseSingleton.INSTANCE.doInTransaction(entityManager -> {
            entityManager.persist(project);
        });

    }

}
