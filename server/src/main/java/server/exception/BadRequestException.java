package server.exception;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends HttpStatusCodeException {
    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST);
    }
}
