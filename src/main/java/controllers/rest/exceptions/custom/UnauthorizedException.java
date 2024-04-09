package controllers.rest.exceptions.custom;

public class UnauthorizedException extends AbstractCustomException{
    public UnauthorizedException(String message, int errorCode, String documentation) {
        super(message, errorCode, documentation);
    }
}
