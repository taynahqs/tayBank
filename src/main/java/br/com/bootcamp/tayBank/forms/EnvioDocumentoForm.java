package br.com.bootcamp.tayBank.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EnvioDocumentoForm {
    @NotNull(message = "Por favor, inclua o Base64 do cpf")
    private String cpf;
}
