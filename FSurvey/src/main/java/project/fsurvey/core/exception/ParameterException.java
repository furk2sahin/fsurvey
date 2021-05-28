package project.fsurvey.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ParameterException extends IllegalArgumentException{
    public ParameterException(String message) {
        super(message);
    }
}
