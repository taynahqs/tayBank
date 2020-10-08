package br.com.bootcamp.tayBank.controllers;

import br.com.bootcamp.tayBank.forms.CadastroClienteForm;
import br.com.bootcamp.tayBank.services.CadastroService;
import br.com.bootcamp.tayBank.utils.ValidationUtils;
import br.com.bootcamp.tayBank.views.CadastroClienteView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ResponseHeader;
import org.hibernate.service.spi.ServiceException;
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
public class CadastroController {

    @Autowired
    CadastroService cadastroService;

    @PostMapping("/cliente")
    @ApiOperation("Cadastro de cliente")
    public ResponseEntity<CadastroClienteView> cadastrarCliente(@RequestBody @Valid CadastroClienteForm cadastroClienteForm) throws ServiceException {
        ValidationUtils.validateCpf(cadastroClienteForm.getCpf());
        ValidationUtils.validateEmail(cadastroClienteForm.getEmail());
        ValidationUtils.validateAge(cadastroClienteForm.getDataNascimento());
        ValidationUtils.validateCnh(cadastroClienteForm.getCnh());

        return cadastroService.cadastrarCliente(cadastroClienteForm);
    }
}
