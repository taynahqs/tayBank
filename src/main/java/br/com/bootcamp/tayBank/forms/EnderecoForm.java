package br.com.bootcamp.tayBank.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class EnderecoForm {
    @Size(min = 10, max = 10)
    @Pattern(regexp = "^\\d{2}\\x2E\\d{3}\\x2D\\d{3}$", message = "Informe o CPF no formato XX.XXX-XXX")
    private String cep;
    private String rua;
    private String bairro;
    private String complemento;
    private String cidade;
    private String estado;
}
