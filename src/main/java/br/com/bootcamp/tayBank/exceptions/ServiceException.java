package br.com.bootcamp.tayBank.exceptions;

import lombok.Getter;

@Getter
public class ServiceException extends Exception {

    private final String message;

    public ServiceException(String message) {
        this.message = message;
    }
}
