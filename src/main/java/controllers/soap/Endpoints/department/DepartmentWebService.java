package controllers.soap.Endpoints.department;

import controllers.rest.beans.PaginationBean;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.ws.BindingType;
import lombok.extern.slf4j.Slf4j;
import models.DTO.DepartmentDto;
import persistence.repositories.helpers.projections.EmployeeProjection;
import services.impl.DepartmentService;

import java.util.List;

@WebService
@Slf4j
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
@BindingType(jakarta.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class DepartmentWebService{

    @WebMethod
    @WebResult(name = "departments")
    public List<DepartmentDto> getDepartments(@WebParam(name = "limit") int limit, @WebParam(name = "offset") int offset) {
        log.info("Getting all departments...");
        try {
            return DepartmentService.getInstance().readAll(offset, limit);
        } catch (Exception e) {
            log.error("Exception occurred while getting all departments", e);
            return List.of();
        }
    }

    @WebMethod
    @WebResult(name = "department")
    public DepartmentDto getDepartment(@WebParam(name = "id") Long id) {
        log.info("Getting department with id: {}...", id);
        try {
            return DepartmentService.getInstance().read(id);
        } catch (Exception e) {
            log.error("Exception occurred while getting department", e);
            return null;
        }
    }

    @WebMethod
    public void createDepartment(@WebParam(name = "department") DepartmentDto departmentDto) {
        log.info("Creating department...");
        try {
            DepartmentService.getInstance().save(departmentDto);
        } catch (Exception e) {
            log.error("Exception occurred while creating department", e);
        }
    }

    @WebMethod
    public void updateDepartment(@WebParam(name = "id") Long id, @WebParam(name = "department") DepartmentDto departmentDto) {
        log.info("Updating department...");
        try {
            DepartmentService.getInstance().update(departmentDto, id);
        } catch (Exception e) {
            log.error("Exception occurred while updating department", e);
        }
    }

    @WebMethod
    public void deleteDepartment(@WebParam(name = "id") Long id) {
        log.info("Deleting department...");
        try {
            DepartmentService.getInstance().delete(id);
        } catch (Exception e) {
            log.error("Exception occurred while deleting department", e);
        }
    }

    @WebMethod
    public void assignManager(@WebParam(name = "departmentId") Long departmentId, @WebParam(name = "managerId") Long managerId) {
        log.info("Assigning manager to department...");
        try {
            DepartmentService.getInstance().updateManager(departmentId, managerId);
        } catch (Exception e) {
            log.error("Exception occurred while assigning manager to department", e);
        }
    }


    @WebMethod
    @WebResult(name = "employees")
    public List<EmployeeProjection> getEmployees(@WebParam(name = "departmentId") Long id, @WebParam(name = "limit") int limit, @WebParam(name = "offset") int offset) {
        log.info("Getting employees of department...");
        try {
            return DepartmentService.getInstance().getEmployees(id, offset, limit);
        } catch (Exception e) {
            log.error("Exception occurred while getting employees of department", e);
            return List.of();
        }
    }
}

