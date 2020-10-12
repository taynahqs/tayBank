package br.com.bootcamp.tayBank.exceptions;

import lombok.Getter;

@Getter
public class MissedStepException extends Exception {

    private final String message;

    public MissedStepException(String message) {
        this.message = message;
    }
}
