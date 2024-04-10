package controllers.rest.exceptions.custom;

import controllers.rest.beans.ErrorMessage;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractCustomException extends RuntimeException {
    ErrorMessage errorMessage;

    protected AbstractCustomException() {
        errorMessage = ErrorMessage.builder()
                .errorCode(999)
                .message("An unknown error occurred")
                .documentation("https://www.example.com/errors/999")
                .build();
    }

    protected AbstractCustomException(String message, int errorCode, String documentation) {
        errorMessage = new ErrorMessage(message, errorCode, documentation);
    }
}
