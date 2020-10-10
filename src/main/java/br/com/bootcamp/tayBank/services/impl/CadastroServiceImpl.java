package br.com.bootcamp.tayBank.services.impl;

import br.com.bootcamp.tayBank.enums.StatusPropostaEnum;
import br.com.bootcamp.tayBank.exceptions.MissedStepException;
import br.com.bootcamp.tayBank.exceptions.ProposalNotFoundException;
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
import br.com.bootcamp.tayBank.utils.ModelMapperUtils;
import br.com.bootcamp.tayBank.utils.SendEmailUtils;
import br.com.bootcamp.tayBank.views.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        cliente.setCpf(form.getCpf());
        cliente.setDataNascimento(form.getDataNascimento());
        cliente.setDataCadastro(LocalDateTime.now());
        cliente.setDataAtualizacao(LocalDateTime.now());
        clienteRepository.save(cliente);

        Proposta proposta = new Proposta();
        proposta.setClienteId(cliente.getId());
        proposta.setCpfCliente(form.getCpf());
        proposta.setDataCadastro(LocalDateTime.now());
        proposta.setDataAtualizacao(LocalDateTime.now());
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

        if(proposta.get().getDocumentoId() != null) {
            throw new ServiceException("Documento já foi enviado!");
        }

        Endereco endereco = new Endereco();
        endereco.setClienteId(clienteId);
        endereco.setCep(form.getCep());
        endereco.setRua(form.getRua());
        endereco.setBairro(form.getBairro());
        endereco.setComplemento(form.getComplemento());
        endereco.setCidade(form.getCidade());
        endereco.setEstado(form.getEstado());
        endereco.setDataCadastro(LocalDateTime.now());
        endereco.setDataAtualizacao(LocalDateTime.now());
        enderecoRepository.save(endereco);

        proposta.get().setEnderecoId(endereco.getId());
        proposta.get().setDataAtualizacao(LocalDateTime.now());
        propostaRepository.save(proposta.get());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/documento/" + proposta.get().getId());

        CadastroEnderecoView view = new CadastroEnderecoView(endereco.getId(), "Endereço cadastrado com sucesso!");

        return new ResponseEntity<>(view, headers, HttpStatus.CREATED);
    }

    public ResponseEntity<EnvioDocumentoView> envioDocumento(EnvioDocumentoForm form, Long propostaId) throws ServiceException, ProposalNotFoundException, MissedStepException {
        Optional<Proposta> proposta = propostaRepository.findById(propostaId);
        if(proposta.isEmpty()){
            throw new ProposalNotFoundException("Proposta não encontrada");
        }

        Long clienteId = proposta.get().getClienteId();

        Optional<Cliente> cliente = clienteRepository.findById(clienteId);
        if(cliente.isEmpty()) {
            throw new MissedStepException("Por favor, volte na etapa de cadastro do cliente");
        }

        List<Endereco> endereco = enderecoRepository.findByClienteId(clienteId);
        if(endereco == null || endereco.isEmpty()) {
            throw new MissedStepException("Por favor, volte na etapa de cadastro do endereço");
        }

        if(!form.getCpf().startsWith("data:image")) {
            throw new ServiceException("Imagem corrompida, por favor, envie novamente");
        }

        Documento documento = new Documento();
        documento.setClienteId(clienteId);
        documento.setDocumento(form.getCpf());
        documento.setDataCadastro(LocalDateTime.now());
        documento.setDataAtualizacao(LocalDateTime.now());
        documentoRepository.save(documento);

        proposta.get().setDocumentoId(documento.getId());
        proposta.get().setDataAtualizacao(LocalDateTime.now());
        proposta.get().setStatus(StatusPropostaEnum.AGUARDANDO_APROVACAO);
        propostaRepository.save(proposta.get());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/dadosProposta/" + proposta.get().getId());

        EnvioDocumentoView view = new EnvioDocumentoView(documento.getId(), "Documento enviado com sucesso!");

        return new ResponseEntity<>(view, headers, HttpStatus.CREATED);
    }

    public ResponseEntity<DadosPropostaView> dadosProposta(Long propostaId) throws ProposalNotFoundException, MissedStepException {
        Optional<Proposta> proposta = propostaRepository.findById(propostaId);
        if(proposta.isEmpty()){
            throw new ProposalNotFoundException("Proposta não encontrada");
        }

        Long clienteId = proposta.get().getClienteId();

        Optional<Cliente> cliente = clienteRepository.findById(clienteId);
        if(cliente.isEmpty()) {
            throw new MissedStepException("Cliente não cadastrado");
        }

        List<Endereco> endereco = enderecoRepository.findByClienteId(clienteId);
        if(endereco == null || endereco.isEmpty()) {
            throw new MissedStepException("Endereço do cliente não encontrado");
        }

        List<Documento> documento = documentoRepository.findByClienteId(clienteId);
        if(documento == null || documento.isEmpty()) {
            throw new MissedStepException("Documento do cliente não enviado");
        }

        ClienteView clienteView = (ClienteView) ModelMapperUtils.convert(cliente.get(), ClienteView.class);
        EnderecoView enderecoView = (EnderecoView) ModelMapperUtils.convert(endereco.get(0), EnderecoView.class);
        DocumentoView documentoView = (DocumentoView) ModelMapperUtils.convert(documento.get(0), DocumentoView.class);

        DadosPropostaView view = new DadosPropostaView(
                proposta.get().getId(),
                clienteView,
                enderecoView,
                documentoView);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/aceite" + proposta.get().getId());


        return new ResponseEntity<>(view, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AceiteView> aceite(Boolean aceite, Long propostaId) throws ProposalNotFoundException, MissedStepException {
        Optional<Proposta> proposta = propostaRepository.findById(propostaId);
        if(proposta.isEmpty()){
            throw new ProposalNotFoundException("Proposta não encontrada");
        }

        Long clienteId = proposta.get().getClienteId();

        Optional<Cliente> cliente = clienteRepository.findById(clienteId);
        if(cliente.isEmpty()) {
            throw new MissedStepException("Cliente não cadastrado");
        }

        List<Endereco> endereco = enderecoRepository.findByClienteId(clienteId);
        if(endereco == null || endereco.isEmpty()) {
            throw new MissedStepException("Endereço do cliente não encontrado");
        }

        List<Documento> documento = documentoRepository.findByClienteId(clienteId);
        if(documento == null || documento.isEmpty()) {
            throw new MissedStepException("Documento do cliente não enviado");
        }

        if(aceite) {
            String para = cliente.get().getEmail();
            String assunto = "Criação de conta - Banco Digital TayBank";
            String mensagem = "Olá, tudo bem?\n\n" +
                    "Agradecemos o aceite da proposta, em breve você receberá os dados de sua conta.\n\n" +
                    "Cordialmente,\n" +
                    "Equipe TayBank";

            SendEmailUtils.SendEmail(para, assunto, mensagem);

            proposta.get().setStatus(StatusPropostaEnum.CRIACAO_CONTA);
            propostaRepository.save(proposta.get());
        } else {
            String para = cliente.get().getEmail();
            String assunto = "Podemos conversar? - Banco Digital TayBank";
            String mensagem = "Olá, tudo bem?\n\n" +
                    "Percebemos que você não aceitou nossa proposta de criação de conta, podemos conversar e entender o motivo," +
                    "tudo no nosso banco é combinável :D.\n" +
                    "Por favor, entre em contato conosco através do 0800 000 0000.\n\n" +
                    "Cordialmente,\n" +
                    "Equipe TayBank";
            SendEmailUtils.SendEmail(para, assunto, mensagem);

            proposta.get().setStatus(StatusPropostaEnum.RECUSADO);
            propostaRepository.save(proposta.get());
        }

        AceiteView view = new AceiteView(propostaId, "Email enviado.");

        return new ResponseEntity<>(view, HttpStatus.OK);
    }
}