package br.com.bootcamp.tayBank.forms;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class CadastroClienteForm {
    @NotNull(message = "Por favor, informe o nome.")
    private String nome;

    @NotNull(message = "Por favor, informe o sobrenome.")
    private String sobrenome;

    @NotNull(message = "Por favor, informe o email.")
    private String email;

    @NotNull(message = "Por favor, informe o cpf")
    @Size(min = 14, max=14)
    @Pattern(regexp = "(^\\d{3}\\x2E\\d{3}\\x2E\\d{3}\\x2D\\d{2}$)", message = "Informe o CPF no formato XXX.XXX.XXX-XX")
    private String cpf;

    @NotNull(message = "Por favor, informe a data de nascimento.")
    private @JsonFormat(pattern = "dd-MM-yyyy") LocalDate dataNascimento;
}
