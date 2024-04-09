package controllers.rest.exceptions.custom;

public class NotModifiedException  extends AbstractCustomException{
    protected NotModifiedException(String message, int errorCode, String documentation) {
        super(message, errorCode, documentation);
    }
}
