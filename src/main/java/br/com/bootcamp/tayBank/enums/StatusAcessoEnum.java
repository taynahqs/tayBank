package br.com.bootcamp.tayBank.enums;

import lombok.Getter;

@Getter
public enum StatusAcessoEnum {
    ATIVO("ATIVO"),
    AGUARDANDO_ATIVACAO("AGUARDANDO_ATIVACAO");

    private final String value;

    StatusAcessoEnum(String value) {
        this.value = value;
    }

    public static StatusAcessoEnum parse(String value) {
        if(value == null) return null;

        for(StatusAcessoEnum e : StatusAcessoEnum.values()) {
            if(e.getValue().equals(value)) {
                return e;
            }
        }
        throw new AssertionError(value);
    }
}
