package br.com.bootcamp.tayBank.services;

import br.com.bootcamp.tayBank.forms.CadastroClienteForm;
import br.com.bootcamp.tayBank.views.CadastroClienteView;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.ResponseEntity;

public interface CadastroService {

    ResponseEntity<CadastroClienteView> cadastrarCliente(CadastroClienteForm cadastroClienteForm) throws ServiceException;
}
