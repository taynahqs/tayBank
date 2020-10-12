package br.com.bootcamp.tayBank.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CadastroEnderecoForm {
    @NotNull(message = "Por favor, informe o CEP")
    @Size(min = 10, max = 10)
    @Pattern(regexp = "^\\d{2}\\x2E\\d{3}\\x2D\\d{3}$", message = "Informe o CPF no formato XX.XXX-XXX")
    private String cep;

    @NotNull(message = "Por favor, informe a rua")
    private String rua;

    @NotNull(message = "Por favor, informe o bairro")
    private String bairro;

    @NotNull(message = "Por favor, informe o complemento")
    private String complemento;

    @NotNull(message = "Por favor, informe a cidade")
    private String cidade;

    @NotNull(message = "Por favor, informe o estado")
    private String estado;
}
