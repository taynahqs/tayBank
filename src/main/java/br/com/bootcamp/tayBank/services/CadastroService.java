package br.com.bootcamp.tayBank.services;

import br.com.bootcamp.tayBank.exceptions.MissedStepException;
import br.com.bootcamp.tayBank.exceptions.ProposalNotFoundException;
import br.com.bootcamp.tayBank.exceptions.ServiceException;
import br.com.bootcamp.tayBank.forms.CadastroClienteForm;
import br.com.bootcamp.tayBank.forms.CadastroEnderecoForm;
import br.com.bootcamp.tayBank.forms.EnvioDocumentoForm;
import br.com.bootcamp.tayBank.views.*;
import org.springframework.http.ResponseEntity;

public interface CadastroService {

    ResponseEntity<CadastroClienteView> cadastrarCliente(CadastroClienteForm cadastroClienteForm) throws ServiceException;

    ResponseEntity<CadastroEnderecoView> cadastrarEndereco(CadastroEnderecoForm cadastroEnderecoForm, Long propostaId) throws ServiceException;

    ResponseEntity<EnvioDocumentoView> envioDocumento(EnvioDocumentoForm envioDocumentoForm, Long propostaId) throws ServiceException, ProposalNotFoundException, MissedStepException;

    ResponseEntity<DadosPropostaView> dadosProposta(Long propostaId) throws ProposalNotFoundException, MissedStepException;

    ResponseEntity<AceiteView> aceite(Boolean aceite, Long propostaId) throws ProposalNotFoundException, MissedStepException;
}
