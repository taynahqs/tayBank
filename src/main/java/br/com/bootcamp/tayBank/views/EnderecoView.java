package br.com.bootcamp.tayBank.views;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoView {
    private String cep;
    private String rua;
    private String bairro;
    private String complemento;
    private String cidade;
    private String estado;
}
