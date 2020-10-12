package br.com.bootcamp.tayBank.controllers;

import br.com.bootcamp.tayBank.exceptions.ServiceException;
import br.com.bootcamp.tayBank.forms.TransferenciaForm;
import br.com.bootcamp.tayBank.services.TransacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api
public class TransacaoController {

    @Autowired
    TransacaoService transacaoService;

    @PostMapping("/transacao/recebeTransferencia")
    @ApiOperation("Recebimento de tranasferência")
    @ResponseStatus(HttpStatus.OK)
    public void recebeTransferencia(@RequestBody @Valid TransferenciaForm transferenciaForm) throws ServiceException {
        transacaoService.recebeTransferencia(transferenciaForm);
    }
}
