package br.com.bootcamp.tayBank.services;

import br.com.bootcamp.tayBank.exceptions.ServiceException;
import br.com.bootcamp.tayBank.forms.AtivaUsuarioForm;
import br.com.bootcamp.tayBank.forms.CriaTokenForm;
import br.com.bootcamp.tayBank.views.TokenView;
import org.springframework.http.ResponseEntity;

public interface AcessoService {
    ResponseEntity<TokenView> token(CriaTokenForm cadastroClienteForm) throws ServiceException;

    void ativaUsuario(AtivaUsuarioForm ativaUsuarioForm) throws ServiceException;
}
