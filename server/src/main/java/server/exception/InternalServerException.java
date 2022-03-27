package server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends HttpStatusCodeException {

    public InternalServerException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
