package controllers.soap.Endpoints.job;


import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.ws.BindingType;
import lombok.extern.slf4j.Slf4j;
import models.DTO.JobDto;
import services.impl.JobService;

import java.util.List;

@WebService
@Slf4j
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
@BindingType(jakarta.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class JobWebService {

    @WebMethod
    @WebResult(name = "job")
    public JobDto createJob(@WebParam(name = "job") JobDto jobDto) {
        log.info("Creating job: {}", jobDto.toString());
        try {
            return JobService.getInstance().save(jobDto);
        } catch (Exception e) {
            log.error("Exception occurred while creating job", e);
            throw new RuntimeException("Exception occurred while creating job");
        }
    }

    @WebMethod
    @WebResult(name = "jobs")
    public List<JobDto> getJobs(@WebParam(name = "offset") int offset, @WebParam(name = "limit") int limit) {
        log.info("Getting all jobs...");
        return JobService.getInstance().readAll(offset, limit);
    }

    @WebMethod
    @WebResult(name = "job")
    public JobDto getJob(@WebParam(name = "id") Long id) {
        log.info("Getting job with id: {}...", id);
        return JobService.getInstance().read(id);
    }

    @WebMethod
    public void updateJob(@WebParam(name = "id") Long id, @WebParam(name = "job") JobDto jobDto) {
        log.info("Updating job with id: {}...", id);
        try {
            if (!JobService.getInstance().update(jobDto, id)) {
                throw new RuntimeException("Failed to update job");
            }
        } catch (Exception e) {
            log.error("Exception occurred while updating job", e);
            throw new RuntimeException("Exception occurred while updating job");
        }
    }

    @WebMethod
    public void deleteJob(@WebParam(name = "id") Long id) {
        log.info("Deleting job with id: {}...", id);
        try {
            if (!JobService.getInstance().delete(id)) {
                throw new RuntimeException("Failed to delete job");
            }
        } catch (Exception e) {
            log.error("Exception occurred while deleting job", e);
            throw new RuntimeException("Exception occurred while deleting job");
        }
    }
}
