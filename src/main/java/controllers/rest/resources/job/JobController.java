package controllers.rest.resources.job;

import controllers.rest.annotations.Secured;
import controllers.rest.beans.PaginationBean;
import controllers.rest.helpers.utils.RestUtil;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import lombok.extern.slf4j.Slf4j;
import models.DTO.JobDto;
import models.enums.Privilege;
import persistence.repositories.helpers.filters.JobFilter;
import services.impl.JobService;

import java.net.URI;
import java.util.List;

@Slf4j
@Path("jobs")
@Secured(value = Privilege.ALL)
public class JobController {
    @Context
    private UriInfo uriInfo;

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
                URI jobUri = RestUtil.getCreatedAtUriForPost(uriInfo, createdJob.getId());
                Link jobLink = Link.fromUri(jobUri).rel("self").build();

                JobResponse jobResponse = new JobResponse();
                jobResponse.setJobDto(createdJob);
                jobResponse.addLink(jobLink);

                GenericEntity<JobResponse> entity = new GenericEntity<>(jobResponse) {
                };
                return Response.created(jobUri).entity(entity).build();
            }
        } catch (Exception e) {
            log.error("Exception occurred while creating job", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception occurred while creating job").build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getJobs(@BeanParam PaginationBean paginationBean,
                            @BeanParam JobFilter filter, @QueryParam("type") String type) {
        log.info("Getting all jobs...");
        List<JobDto> jobs = JobService.getInstance().readAll(paginationBean.getOffset(), paginationBean.getLimit(), filter);
        JobResponseWrapper jobResponseWrapper = new JobResponseWrapper();
        for (JobDto job : jobs) {
            JobResponse jobResponse = new JobResponse();
            jobResponse.setJobDto(job);
            jobResponse.addLink(RestUtil.createSelfLink(uriInfo, job.getId(), JobController.class));
            jobResponseWrapper.getJobs().add(jobResponse);
        }
        for (Link link : RestUtil.createPaginatedResourceLink(uriInfo, paginationBean, JobService.getInstance().count())) {
            jobResponseWrapper.addLink(link);
        }
        return buildResponse(jobResponseWrapper, type);
    }

    private Response buildResponse(JobResponseWrapper jobResponseWrapper, String type) {
        GenericEntity<JobResponseWrapper> entity = new GenericEntity<>(jobResponseWrapper) {
        };
        if (type != null && type.equals("xml")) {
            return Response.ok(entity, MediaType.APPLICATION_XML).build();
        } else {
            return Response.ok(entity, MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getJob(@PathParam("id") Long id, @QueryParam("type") String type) {
        log.info("Getting job with id: {}...", id);
        JobDto job = JobService.getInstance().read(id);
        JobResponse jobResponse = new JobResponse();
        jobResponse.setJobDto(job);
        jobResponse.addLink(RestUtil.createSelfLink(uriInfo, id, JobController.class));
        return buildResponse(jobResponse, type);
    }

    private Response buildResponse(JobResponse jobResponse, String type) {
        GenericEntity<JobResponse> entity = new GenericEntity<>(jobResponse) {
        };
        if (type != null && type.equals("xml")) {
            return Response.ok(entity, MediaType.APPLICATION_XML).build();
        } else {
            return Response.ok(entity, MediaType.APPLICATION_JSON).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateJob(@PathParam("id") Long id, JobDto jobDto) {
        log.info("Updating job with id: {}...", id);
        try {
            boolean updatedJob = JobService.getInstance().update(jobDto, id);
            if (!updatedJob) {
                log.error("Failed to update job");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to update job").build();
            } else {
                log.info("Job updated successfully");
                return getResponse(id, jobDto);
            }
        } catch (Exception e) {
            log.error("Exception occurred while updating job", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception occurred while updating job").build();
        }
    }

    @PATCH
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response partialUpdateJob(@PathParam("id") Long id, JobDto jobDto) {
        log.info("Patching job with id: {}...", id);
        try {
            boolean updatedJob = JobService.getInstance().update(jobDto, id);
            if (!updatedJob) {
                log.error("Failed to patch job");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to patch job").build();
            } else {
                log.info("Job patched successfully");
                return getResponse(id, jobDto);
            }
        } catch (Exception e) {
            log.error("Exception occurred while patching job", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception occurred while patching job").build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteJob(@PathParam("id") Long id) {
        log.info("Deleting job with id: {}...", id);
        try {
            boolean deletedJob = JobService.getInstance().delete(id);
            if (!deletedJob) {
                log.error("Failed to delete job");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to delete job").build();
            } else {
                log.info("Job deleted successfully");
                return Response.ok().build();
            }
        } catch (Exception e) {
            log.error("Exception occurred while deleting job", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Exception occurred while deleting job").build();
        }
    }

    private Response getResponse(Long id, JobDto jobDto) {
        Link jobLink = RestUtil.createSelfLink(uriInfo, id, JobController.class);

        JobResponse jobResponse = new JobResponse();
        jobResponse.setJobDto(jobDto);
        jobResponse.addLink(jobLink);

        GenericEntity<JobResponse> entity = new GenericEntity<>(jobResponse) {
        };
        return Response.ok(entity).build();
    }


}
