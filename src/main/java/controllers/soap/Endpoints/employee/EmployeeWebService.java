package controllers.soap.Endpoints.employee;


import controllers.rest.beans.PaginationBean;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.ws.BindingType;
import lombok.extern.slf4j.Slf4j;
import models.DTO.EmployeeDto;
import persistence.repositories.helpers.projections.EmployeeProjection;
import services.impl.AuthenticationService;
import services.impl.EmployeeService;
import utils.ApiUtil;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
@BindingType(jakarta.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@WebService
public class EmployeeWebService {

    @WebMethod
    @WebResult(name = "employee")
    public EmployeeProjection getEmployee(@WebParam(name = "id") Long id , @WebParam(name = "fields") String fields){
        Set<String> fieldSet = ApiUtil.getFields(fields);
        log.info("Fields: {}", fieldSet);
        return EmployeeService.getInstance().employeePartialResponse(id, fieldSet);
    }

    @WebMethod
    @WebResult(name = "employees")
    public List<EmployeeProjection> getEmployees(@WebParam(name = "limit") int limit, @WebParam(name = "offset") int offset , @WebParam(name = "fields") String fields) {
        log.info("Getting all employees...");
        Set<String> fieldSet = ApiUtil.getFields(fields);
        return EmployeeService.getInstance().getAllEmployeesPartialResponse(fieldSet, offset, limit);
    }

    @WebMethod
    @WebResult(name = "employee")
    public EmployeeDto createEmployee(@WebParam(name = "employee") EmployeeDto employeeDto) {
        if ((employeeDto.getHireDate() != null && employeeDto.getHireDate().isAfter(LocalDate.now())) ||
                (employeeDto.getBirthDate() != null && employeeDto.getBirthDate().isAfter(LocalDate.now()))) {
            throw new IllegalArgumentException("Hire date and birth date should be in the past or present");
        }
        return AuthenticationService.getInstance().save(employeeDto);
    }

    @WebMethod
    public void updateEmployee(@WebParam(name = "id") Long id, @WebParam(name = "employee") EmployeeDto employeeDto) {
        if ((employeeDto.getHireDate() != null && employeeDto.getHireDate().isAfter(LocalDate.now())) ||
                (employeeDto.getBirthDate() != null && employeeDto.getBirthDate().isAfter(LocalDate.now()))) {
            throw new IllegalArgumentException("Hire date and birth date should be in the past or present");
        }
        EmployeeService.getInstance().update(employeeDto, id);
    }

    @WebMethod
    public void deleteEmployee(@WebParam(name = "id") Long id, @WebParam(name = "isFired") boolean isFired) {
        log.info("Deleting employee with id: {} isFired: {}", id, isFired);
        boolean deleted = EmployeeService.getInstance().delete(id, isFired);
        if (!deleted) {
            throw new IllegalArgumentException("Employee not found");
        }
    }

    @WebMethod
    public void assignManager(@WebParam(name = "employeeId") Long employeeId, @WebParam(name = "managerId") Long managerId) {
        log.info("Assigning manager with id: {} to employee with id: {}", managerId, employeeId);
        boolean assigned = EmployeeService.getInstance().assignManager(employeeId, managerId);
        if (!assigned) {
            throw new IllegalArgumentException("Failed to assign manager");
        }
    }

    @WebMethod
    @WebResult(name = "manager")
    public EmployeeProjection getManager(@WebParam(name = "id") Long id) {
        log.info("Getting manager for employee with id: {}", id);
        EmployeeProjection manager = EmployeeService.getInstance().getManager(id);
        if (manager == null) {
            throw new IllegalArgumentException("Manager not found");
        }
        return manager;
    }
}
