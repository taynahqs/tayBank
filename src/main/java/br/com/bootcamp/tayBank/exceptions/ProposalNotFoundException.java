package br.com.bootcamp.tayBank.exceptions;

import lombok.Getter;

@Getter
public class ProposalNotFoundException extends Exception {

    private final String message;

    public ProposalNotFoundException(String message) {
        this.message = message;
    }
}
