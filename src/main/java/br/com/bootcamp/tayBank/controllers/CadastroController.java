package br.com.bootcamp.tayBank.controllers;

import br.com.bootcamp.tayBank.exceptions.MissedStepException;
import br.com.bootcamp.tayBank.exceptions.ProposalNotFoundException;
import br.com.bootcamp.tayBank.exceptions.ServiceException;
import br.com.bootcamp.tayBank.forms.CadastroClienteForm;
import br.com.bootcamp.tayBank.forms.CadastroEnderecoForm;
import br.com.bootcamp.tayBank.forms.EnvioDocumentoForm;
import br.com.bootcamp.tayBank.services.CadastroService;
import br.com.bootcamp.tayBank.utils.ValidationUtils;
import br.com.bootcamp.tayBank.views.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ResponseHeader;
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

        return cadastroService.cadastrarCliente(cadastroClienteForm);
    }

    @PostMapping("/endereco/{propostaId}")
    @ApiOperation("Cadastro do endereço do cliente.")
    public ResponseEntity<CadastroEnderecoView> cadastrarEndereco(@RequestBody @Valid CadastroEnderecoForm cadastroEnderecoForm, @PathVariable Long propostaId) throws ServiceException {
        return cadastroService.cadastrarEndereco(cadastroEnderecoForm, propostaId);
    }

    @PostMapping("/documento/{propostaId}")
    @ApiOperation("Envio de foto do documento")
    public ResponseEntity<EnvioDocumentoView> envioDocumento(@RequestBody @Valid EnvioDocumentoForm envioDocumentoForm, @PathVariable Long propostaId) throws ServiceException, ProposalNotFoundException, MissedStepException {
        return cadastroService.envioDocumento(envioDocumentoForm, propostaId);
    }

    @GetMapping("/dadosProposta/{propostaId}")
    @ApiOperation("Confirmação dos dados da proposta")
    public ResponseEntity<DadosPropostaView> dadosProposta(@PathVariable Long propostaId) throws MissedStepException, ProposalNotFoundException {
        return cadastroService.dadosProposta(propostaId);
    }

    @PutMapping("/aceite/{propostaId}")
    @ApiOperation("Confirmação e aceite da proposta")
    public ResponseEntity<AceiteView> aceite(@RequestHeader Boolean aceite, @PathVariable Long propostaId) throws ServiceException, ProposalNotFoundException, MissedStepException {
        return cadastroService.aceite(aceite, propostaId);
    }
}