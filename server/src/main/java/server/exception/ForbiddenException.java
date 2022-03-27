package server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenException extends HttpStatusCodeException {
    public ForbiddenException(){
        super(HttpStatus.FORBIDDEN);
    }

}
