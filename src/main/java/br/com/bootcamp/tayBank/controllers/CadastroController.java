package br.com.bootcamp.tayBank.controllers;

import br.com.bootcamp.tayBank.exceptions.ServiceException;
import br.com.bootcamp.tayBank.forms.CadastroClienteForm;
import br.com.bootcamp.tayBank.forms.CadastroEnderecoForm;
import br.com.bootcamp.tayBank.forms.EnvioDocumentoForm;
import br.com.bootcamp.tayBank.services.CadastroService;
import br.com.bootcamp.tayBank.utils.ValidationUtils;
import br.com.bootcamp.tayBank.views.CadastroClienteView;
import br.com.bootcamp.tayBank.views.CadastroEnderecoView;
import br.com.bootcamp.tayBank.views.EnvioDocumentoView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        ValidationUtils.validateNumber(cadastroClienteForm.getCnh());

        return cadastroService.cadastrarCliente(cadastroClienteForm);
    }

    @PostMapping("/endereco/{propostaId}")
    @ApiOperation("Cadastro do endereço do cliente.")
    public ResponseEntity<CadastroEnderecoView> cadastrarEndereco(@RequestBody @Valid CadastroEnderecoForm cadastroEnderecoForm, @PathVariable Long propostaId) throws ServiceException {
        ValidationUtils.validateNumber(cadastroEnderecoForm.getCep());
        return cadastroService.cadastrarEndereco(cadastroEnderecoForm, propostaId);
    }

    @PostMapping("/documento/{propostaId}")
    @ApiOperation("Envio de foto do documento")
    public ResponseEntity<EnvioDocumentoView> envioDocumento(@RequestBody @Valid EnvioDocumentoForm envioDocumentoForm, @PathVariable Long propostaId) throws ServiceException {
        return cadastroService.envioDocumento(envioDocumentoForm, propostaId);
    }
}
