package br.com.bootcamp.tayBank.services.impl;

import br.com.bootcamp.tayBank.models.Cliente;
import br.com.bootcamp.tayBank.forms.CadastroClienteForm;
import br.com.bootcamp.tayBank.repositories.ClienteRepository;
import br.com.bootcamp.tayBank.services.CadastroService;
import br.com.bootcamp.tayBank.views.CadastroClienteView;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Service
public class CadastroServiceImpl implements CadastroService {

    @Autowired
    ClienteRepository clienteRepository;

    @Override
    public ResponseEntity<CadastroClienteView> cadastrarCliente(CadastroClienteForm form) throws ServiceException {
        List<Cliente> clienteEmail = clienteRepository.findByEmail(form.getEmail());
        if(clienteEmail != null && !clienteEmail.isEmpty()) {
            throw new ServiceException("Email já utilizado.");
        }

        List<Cliente> clienteCpf = clienteRepository.findByCpf(form.getCpf());
        if(clienteCpf != null && !clienteCpf.isEmpty()) {
            throw new ServiceException("CPF já utilizado.");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(form.getNome());
        cliente.setSobrenome(form.getSobrenome());
        cliente.setEmail(form.getEmail());
        cliente.setCnh(form.getCnh());
        cliente.setCpf(form.getCpf());
        cliente.setDataNascimento(form.getDataNascimento());
        clienteRepository.save(cliente);

        CadastroClienteView view = new CadastroClienteView();
        view.setId(cliente.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/endereco");

        return new ResponseEntity<>(view, headers, HttpStatus.CREATED);
    }
}
