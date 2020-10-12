package br.com.bootcamp.tayBank.views;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DadosPropostaView {
    private Long numeroProposta;
    private ClienteView cliente;
    private EnderecoView endereco;
    private DocumentoView documento;
}
