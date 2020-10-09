package br.com.bootcamp.tayBank.services;

import br.com.bootcamp.tayBank.exceptions.ServiceException;
import br.com.bootcamp.tayBank.forms.CadastroClienteForm;
import br.com.bootcamp.tayBank.forms.CadastroEnderecoForm;
import br.com.bootcamp.tayBank.forms.EnvioDocumentoForm;
import br.com.bootcamp.tayBank.views.CadastroClienteView;
import br.com.bootcamp.tayBank.views.CadastroEnderecoView;
import br.com.bootcamp.tayBank.views.DadosPropostaView;
import br.com.bootcamp.tayBank.views.EnvioDocumentoView;
import org.springframework.http.ResponseEntity;

public interface CadastroService {

    ResponseEntity<CadastroClienteView> cadastrarCliente(CadastroClienteForm cadastroClienteForm) throws ServiceException;

    ResponseEntity<CadastroEnderecoView> cadastrarEndereco(CadastroEnderecoForm cadastroEnderecoForm, Long propostaId) throws ServiceException;

    ResponseEntity<EnvioDocumentoView> envioDocumento(EnvioDocumentoForm envioDocumentoForm, Long propostaId) throws ServiceException;

    ResponseEntity<DadosPropostaView> dadosProposta(Long propostaId) throws ServiceException;
}
