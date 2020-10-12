package br.com.bootcamp.tayBank.forms;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
public class ClienteForm {
    private String nome;
    private String sobrenome;
    private String email;
    @Size(min = 14, max=14)
    @Pattern(regexp = "(^\\d{3}\\x2E\\d{3}\\x2E\\d{3}\\x2D\\d{2}$)", message = "Informe o CPF no formato XXX.XXX.XXX-XX")
    private String cpf;
    private @JsonFormat(pattern = "dd-MM-yyyy") LocalDate dataNascimento;
}
