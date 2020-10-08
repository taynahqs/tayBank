package br.com.bootcamp.tayBank.services.impl;

import br.com.bootcamp.tayBank.exceptions.ServiceException;
import br.com.bootcamp.tayBank.forms.CadastroEnderecoForm;
import br.com.bootcamp.tayBank.forms.EnvioDocumentoForm;
import br.com.bootcamp.tayBank.models.Cliente;
import br.com.bootcamp.tayBank.forms.CadastroClienteForm;
import br.com.bootcamp.tayBank.models.Documento;
import br.com.bootcamp.tayBank.models.Endereco;
import br.com.bootcamp.tayBank.models.Proposta;
import br.com.bootcamp.tayBank.repositories.ClienteRepository;
import br.com.bootcamp.tayBank.repositories.DocumentoRepository;
import br.com.bootcamp.tayBank.repositories.EnderecoRepository;
import br.com.bootcamp.tayBank.repositories.PropostaRepository;
import br.com.bootcamp.tayBank.services.CadastroService;
import br.com.bootcamp.tayBank.views.CadastroClienteView;
import br.com.bootcamp.tayBank.views.CadastroEnderecoView;
import br.com.bootcamp.tayBank.views.EnvioDocumentoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CadastroServiceImpl implements CadastroService {

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    EnderecoRepository enderecoRepository;

    @Autowired
    DocumentoRepository documentoRepository;

    @Autowired
    PropostaRepository propostaRepository;

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

        Proposta proposta = new Proposta();
        proposta.setClienteId(cliente.getId());
        propostaRepository.save(proposta);

        CadastroClienteView view = new CadastroClienteView(cliente.getId(), "Cliente cadastrado com sucesso!");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/endereco/" + proposta.getId());

        return new ResponseEntity<>(view, headers, HttpStatus.CREATED);
    }

    public ResponseEntity<CadastroEnderecoView> cadastrarEndereco(CadastroEnderecoForm form, Long propostaId) throws ServiceException {
        Optional<Proposta> proposta = propostaRepository.findById(propostaId);
        if(proposta.isEmpty()){
            throw new ServiceException("Proposta não encontrada");
        }

        Long clienteId = proposta.get().getClienteId();

        Optional<Cliente> cliente = clienteRepository.findById(clienteId);
        if(cliente.isEmpty()) {
            throw new ServiceException("Cliente não cadastrado");
        }
        //formatando o CEP
        String cep1Parte = form.getCep().substring(0,5);
        String cep2Parte = form.getCep().substring(5);
        String cep = cep1Parte.concat("-").concat(cep2Parte);

        Endereco endereco = new Endereco();
        endereco.setClienteId(clienteId);
        endereco.setCep(cep);
        endereco.setRua(form.getRua());
        endereco.setBairro(form.getBairro());
        endereco.setComplemento(form.getComplemento());
        endereco.setCidade(form.getCidade());
        endereco.setEstado(form.getEstado());
        enderecoRepository.save(endereco);

        proposta.get().setEnderecoId(endereco.getId());
        propostaRepository.save(proposta.get());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/documento/" + proposta.get().getId());

        CadastroEnderecoView view = new CadastroEnderecoView(endereco.getId(), "Endereço cadastrado com sucesso!");

        return new ResponseEntity<>(view, headers, HttpStatus.CREATED);
    }

    public ResponseEntity<EnvioDocumentoView> envioDocumento(EnvioDocumentoForm form, Long propostaId) throws ServiceException {
        Optional<Proposta> proposta = propostaRepository.findById(propostaId);
        if(proposta.isEmpty()){
            throw new ServiceException("Proposta não encontrada");
        }

        Long clienteId = proposta.get().getClienteId();

        Optional<Cliente> cliente = clienteRepository.findById(clienteId);
        if(cliente.isEmpty()) {
            throw new ServiceException("Cliente não cadastrado");
        }

        List<Endereco> endereco = enderecoRepository.findByClienteId(clienteId);
        if(endereco == null || endereco.isEmpty()) {
            throw new ServiceException("Endereço do cliente não encontrado");
        }

        if(!form.getCnhFrente().startsWith("data:image") || !form.getCnhVerso().startsWith("data:image")) {
            throw new ServiceException("Imagens corrompidas, por favor, envie novamente");
        }

        Documento documento = new Documento();
        documento.setClienteId(clienteId);
        documento.setDocumentoFrente(form.getCnhFrente());
        documento.setDocumentoVerso(form.getCnhVerso());
        documentoRepository.save(documento);

        proposta.get().setDocumentoId(documento.getId());
        propostaRepository.save(proposta.get());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/dadosProposta/" + proposta.get().getId());

        EnvioDocumentoView view = new EnvioDocumentoView(documento.getId(), "Documento enviado com sucesso!");

        return new ResponseEntity<>(view, headers, HttpStatus.CREATED);
    }
}