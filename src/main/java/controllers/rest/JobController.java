package controllers.rest;

import controllers.rest.annotations.Secured;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import models.DTO.JobDto;
import models.enums.JobTitle;
import models.enums.Privilege;
import persistence.repositories.helpers.filters.JobFilter;
import services.impl.JobService;

import java.math.BigDecimal;

@Slf4j
@Path("jobs")
@Secured(value = Privilege.ALL)
public class JobController {

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Secured(value = Privilege.ALL)
    public Response createJob(JobDto jobDto) {
        log.info("Creating job + {}...", jobDto.toString());
        try {
            JobDto createdJob = JobService.getInstance().save(jobDto);
            if (createdJob == null) {
                log.error("Failed to create job");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to create job").build();
            } else {
                log.info("Job created successfully");
                GenericEntity<JobDto> entity = new GenericEntity<>(createdJob) {
                };
                return Response.ok(entity).build();
            }
        } catch (Exception e) {
            log.error("Exception occurred while creating job", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception occurred while creating job").build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getJobs(@QueryParam("offset") @DefaultValue("0") int offset,
                            @QueryParam("limit") @DefaultValue("10") int limit,
                            @QueryParam("name") String name,
                            @QueryParam("sortOrder") String sortOrder,
                            @QueryParam("sortBy") String sortBy,
                            @QueryParam("title") JobTitle title,
                            @QueryParam("minSalary") BigDecimal minSalary,
                            @QueryParam("maxSalary") BigDecimal maxSalary,
                            @QueryParam("minExperience") Integer minExperience) {
        log.info("Getting all jobs...");
        try {
            JobFilter filter = new JobFilter.Builder()
                    .name(name)
                    .sortOrder(sortOrder)
                    .sortBy(sortBy)
                    .title(title)
                    .minSalary(minSalary)
                    .maxSalary(maxSalary)
                    .minExperience(minExperience)
                    .build();
            return Response.ok(JobService.getInstance().readAll(offset, limit, filter)).build();
        } catch (Exception e) {
            log.error("Exception occurred while getting all jobs", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception occurred while getting all jobs").build();
        }
    }



}
