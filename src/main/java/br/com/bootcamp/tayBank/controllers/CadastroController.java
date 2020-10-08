package br.com.bootcamp.tayBank.controllers;

import br.com.bootcamp.tayBank.forms.CadastroClienteForm;
import br.com.bootcamp.tayBank.services.CadastroService;
import br.com.bootcamp.tayBank.utils.ValidationUtils;
import br.com.bootcamp.tayBank.views.CadastroClienteView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class CadastroController {

    @Autowired
    CadastroService cadastroService;

    @PostMapping("/cliente")
   //@ApiOperator()
    public ResponseEntity<CadastroClienteView> cadastrarCliente(@RequestBody @Valid CadastroClienteForm cadastroClienteForm) {
        ValidationUtils.validateCpf(cadastroClienteForm.getCpf());
        ValidationUtils.validateEmail(cadastroClienteForm.getEmail());
        ValidationUtils.validateAge(cadastroClienteForm.getDataNascimento());

        return cadastroService.cadastrarCliente(cadastroClienteForm);
    }
}
