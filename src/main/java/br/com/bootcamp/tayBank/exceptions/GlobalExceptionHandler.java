package br.com.bootcamp.tayBank.exceptions;

import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ServiceException.class})
    private ResponseEntity<Object> serviceException(ServiceException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<String> message = new ArrayList<>();
        message.add(ex.getMessage());
        ApiError apiError = new ApiError(status, message);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = createErrorList(ex.getBindingResult());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    private List<String> createErrorList(BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();
        for(FieldError fieldError : bindingResult.getFieldErrors()) {
            String message = fieldError.getField()+ ": " + fieldError.getDefaultMessage();
            errors.add(message);
        }
        return errors;
    }

    @Getter
    public static class Error {
        private String message;

        Error(String message) {
            this.message = message;
        }
    }
}
