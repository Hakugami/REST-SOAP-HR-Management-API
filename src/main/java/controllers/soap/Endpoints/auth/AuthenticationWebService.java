package controllers.soap.Endpoints.auth;


import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.ws.BindingType;
import jakarta.xml.ws.RequestWrapper;
import jakarta.xml.ws.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import models.DTO.LoginDto;
import services.impl.AuthenticationService;

@WebService
@Slf4j
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
@BindingType(jakarta.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AuthenticationWebService {

    @WebMethod
    @RequestWrapper(className = "LoginRequest")
    @ResponseWrapper(className = "LoginResponse")
    @WebResult(name = "Token")
    public String login(@WebParam(name = "LoginRequest") LoginDto loginRequest) {
        String loginToken = AuthenticationService.getInstance().login(loginRequest.getUsername(), loginRequest.getPassword());
        return loginToken != null ? loginToken : "";
    }
}
