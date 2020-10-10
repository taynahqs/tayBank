package br.com.bootcamp.tayBank.enums;

import lombok.Getter;

@Getter
public enum StatusPropostaEnum {
    AGUARDANDO_APROVACAO("AGUARDANDO_APROVACAO"),
    APROVADO("APROVADO"),
    RECUSADO("RECUSADO"),
    CRIACAO_CONTA("CRIACAO_CONTA");

    private final String value;

    StatusPropostaEnum(String value) {
        this.value = value;
    }

    public static StatusPropostaEnum parse(String value) {
        if(value == null) return null;

        for(StatusPropostaEnum e : StatusPropostaEnum.values()) {
            if(e.getValue().equals(value)) {
                return e;
            }
        }
        throw new AssertionError(value);
    }
}
