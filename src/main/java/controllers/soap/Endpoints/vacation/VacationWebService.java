package controllers.soap.Endpoints.vacation;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.ws.BindingType;
import lombok.extern.slf4j.Slf4j;
import models.DTO.VacationDto;
import services.impl.VacationService;


@WebService
@Slf4j
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
@BindingType(jakarta.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class VacationWebService {

    @WebMethod
    public String requestVacation(@WebParam(name = "email") String email, @WebParam(name = "vacationDto") VacationDto vacationDto) {
        try {
            boolean result = VacationService.getInstance().requestVacation(email, vacationDto);
            if (result) {
                return "Vacation requested successfully";
            } else {
                return "Vacation request failed";
            }
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @WebMethod
    public String approveVacationRequest(@WebParam(name = "id") Long id) {
        boolean result = VacationService.getInstance().approveVacation(id);
        if (result) {
            return "Vacation request approved";
        } else {
            return "Vacation request approval failed";
        }
    }

    @WebMethod
    public String rejectVacationRequest(@WebParam(name = "id") Long id) {
        boolean result = VacationService.getInstance().rejectVacation(id);
        if (result) {
            return "Vacation request rejected";
        } else {
            return "Vacation request rejection failed";
        }
    }
}
