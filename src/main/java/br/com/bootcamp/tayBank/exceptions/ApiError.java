package br.com.bootcamp.tayBank.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
public class ApiError {

    private final HttpStatus status;
    private final List<String> message;

    public ApiError(HttpStatus status, List<String> message) {
        this.status = status;
        this.message = message;
    }
}
