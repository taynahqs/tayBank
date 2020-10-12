package br.com.bootcamp.tayBank.forms;

import br.com.bootcamp.tayBank.views.ClienteView;
import br.com.bootcamp.tayBank.views.DocumentoView;
import br.com.bootcamp.tayBank.views.EnderecoView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DadosPropostaForm {
    private Long numeroProposta;
    private ClienteView cliente;
    private EnderecoView endereco;
    private DocumentoView documento;
}
