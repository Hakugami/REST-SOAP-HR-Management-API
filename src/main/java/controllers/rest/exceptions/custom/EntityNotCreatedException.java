package controllers.rest.exceptions.custom;

public class EntityNotCreatedException extends AbstractCustomException{
    public EntityNotCreatedException(String message, int errorCode, String documentation) {
        super(message, errorCode, documentation);
    }
}
