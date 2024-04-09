package controllers.rest.exceptions.custom;

public class ResourceNotFoundException extends AbstractCustomException {
    public ResourceNotFoundException(String message, int errorCode, String documentation) {
        super(message, errorCode, documentation);
    }
}
