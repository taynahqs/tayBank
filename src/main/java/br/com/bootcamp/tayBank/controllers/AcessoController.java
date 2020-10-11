package br.com.bootcamp.tayBank.controllers;

import br.com.bootcamp.tayBank.exceptions.ServiceException;
import br.com.bootcamp.tayBank.forms.AtivaUsuarioForm;
import br.com.bootcamp.tayBank.forms.CadastroClienteForm;
import br.com.bootcamp.tayBank.forms.CriaTokenForm;
import br.com.bootcamp.tayBank.services.AcessoService;
import br.com.bootcamp.tayBank.utils.ValidationUtils;
import br.com.bootcamp.tayBank.views.CadastroClienteView;
import br.com.bootcamp.tayBank.views.TokenView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api
public class AcessoController {

    @Autowired
    AcessoService acessoService;

    @PostMapping("/token")
    @ApiOperation("Envio de token de acesso por email")
    public ResponseEntity<TokenView> token(@RequestBody @Valid CriaTokenForm cadastroClienteForm) throws ServiceException {
        ValidationUtils.validateCpf(cadastroClienteForm.getCpf());
        ValidationUtils.validateEmail(cadastroClienteForm.getEmail());

        return acessoService.token(cadastroClienteForm);
    }

    @PostMapping("/ativaUsuario")
    @ApiOperation("Ativar usuario")
    @ResponseStatus(HttpStatus.OK)
    public void ativaUsuario(@RequestBody @Valid AtivaUsuarioForm ativaUsuarioForm) throws ServiceException {
        ValidationUtils.validateCpf(ativaUsuarioForm.getCpf());
        ValidationUtils.validatePassword(ativaUsuarioForm.getSenha());
        acessoService.ativaUsuario(ativaUsuarioForm);
    }
}
