package br.com.bootcamp.tayBank.forms;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class CadastroClienteForm {
    @NotNull(message = "Por favor, informe o nome.")
    private String nome;

    @NotNull(message = "Por favor, informe o sobrenome.")
    private String sobrenome;

    @NotNull(message = "Por favor, informe o email.")
    private String email;

    @NotNull(message = "Por favor, informe a cnh.")
    private String cnh;

    @NotNull(message = "Por favor, informe o cpf")
    private String cpf;

    @NotNull(message = "Por favor, informe a data de nascimento.")
    private @JsonFormat(pattern = "dd-MM-yyyy") LocalDate dataNascimento;
}
