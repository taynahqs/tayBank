package br.com.bootcamp.tayBank.services.impl;

import br.com.bootcamp.tayBank.entities.ClienteEntity;
import br.com.bootcamp.tayBank.forms.CadastroClienteForm;
import br.com.bootcamp.tayBank.repositories.ClienteRepository;
import br.com.bootcamp.tayBank.services.CadastroService;
import br.com.bootcamp.tayBank.views.CadastroClienteView;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CadastroServiceImpl implements CadastroService {

    @Autowired
    ClienteRepository clienteRepository;

    @Override
    public ResponseEntity<CadastroClienteView> cadastrarCliente(CadastroClienteForm form) {
        List<ClienteEntity> clienteEmail = clienteRepository.findByEmail(form.getEmail());

        if(clienteEmail != null && !clienteEmail.isEmpty()) {
            throw new ServiceException("Email já utilizado.");
        }

        ClienteEntity cliente = new ClienteEntity();
        cliente.setNome(form.getNome());
        cliente.setSobrenome(form.getSobrenome());
        cliente.setEmail(form.getEmail());
        cliente.setCnh(form.getCnh());
        cliente.setDataNascimento(form.getDataNascimento());
        clienteRepository.save(cliente);

        CadastroClienteView view = new CadastroClienteView();
        view.setId(cliente.getId());

        return new ResponseEntity<>(view, HttpStatus.CREATED);
    }
}
