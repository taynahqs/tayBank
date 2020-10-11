package br.com.bootcamp.tayBank.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AtivaUsuarioForm {

    @NotNull(message = "Infome o CPF")
    @Size(min = 14, max=14)
    @Pattern(regexp = "(^\\d{3}\\x2E\\d{3}\\x2E\\d{3}\\x2D\\d{2}$)", message = "Informe o CPF no formato XXX.XXX.XXX-XX")
    private String cpf;

    @NotNull(message = "Infome o token")
    private String token;

    @Size(min = 8, max = 8)
    @NotNull(message = "Infome a senha")
    private String senha;

    @Size(min = 8, max = 8)
    @NotNull(message = "Infome a confirmação da senha")
    private String confirmaSenha;
}