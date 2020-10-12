package br.com.bootcamp.tayBank.services.impl;

import br.com.bootcamp.tayBank.enums.StatusPropostaEnum;
import br.com.bootcamp.tayBank.exceptions.MissedStepException;
import br.com.bootcamp.tayBank.exceptions.ProposalNotFoundException;
import br.com.bootcamp.tayBank.exceptions.ServiceException;
import br.com.bootcamp.tayBank.forms.CadastroClienteForm;
import br.com.bootcamp.tayBank.forms.CadastroEnderecoForm;
import br.com.bootcamp.tayBank.forms.DadosPropostaForm;
import br.com.bootcamp.tayBank.forms.EnvioDocumentoForm;
import br.com.bootcamp.tayBank.model.Cliente;
import br.com.bootcamp.tayBank.model.Documento;
import br.com.bootcamp.tayBank.model.Endereco;
import br.com.bootcamp.tayBank.model.Proposta;
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
        Cliente clienteEmail = clienteRepository.findByEmail(form.getEmail());
        if(clienteEmail != null) {
            throw new ServiceException("Email já utilizado.");
        }

        Cliente clienteCpf = clienteRepository.findByCpf(form.getCpf());
        if(clienteCpf != null) {
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
        headers.add(HttpHeaders.LOCATION, "/cadastro/endereco/" + proposta.getId());

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

        List<Endereco> enderecoList = enderecoRepository.findByClienteId(clienteId);
        if(enderecoList != null && !enderecoList.isEmpty()){
            throw new ServiceException("Endereço já cadastrado");
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
        headers.add(HttpHeaders.LOCATION, "/cadastro/documento/" + proposta.get().getId());

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
        headers.add(HttpHeaders.LOCATION, "/cadastro/dadosProposta/" + proposta.get().getId());

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
        headers.add(HttpHeaders.LOCATION, "/cadastro/dadosProposta/" + proposta.get().getId());


        return new ResponseEntity<>(view, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AceiteView> aceite(Boolean aceite, Long propostaId) throws ProposalNotFoundException, MissedStepException {
        //validacao se todos os passos foram preenchidos
        validaPassos(propostaId);

        Optional<Proposta> proposta = propostaRepository.findById(propostaId);
        Optional<Cliente> cliente = clienteRepository.findById(proposta.get().getClienteId());

        //envio de email de acordo com a resposta da proposta
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

    @Override
    public ResponseEntity<DadosPropostaView> editaDadosProposta(DadosPropostaForm form, Long propostaId) throws ServiceException, ProposalNotFoundException, MissedStepException {
        //validacao se todos os passos foram preenchidos
        validaPassos(propostaId);

        Optional<Proposta> proposta = propostaRepository.findById(propostaId);
        Long clienteId = proposta.get().getClienteId();

        Optional<Cliente> cliente = clienteRepository.findById(clienteId);
        if(form.getCliente() != null) {
            ClienteView clienteForm = form.getCliente();
            cliente.get().setNome(clienteForm.getNome() == null ? cliente.get().getNome() : clienteForm.getNome());
            cliente.get().setSobrenome(clienteForm.getSobrenome() == null ? cliente.get().getSobrenome() : clienteForm.getSobrenome());
            cliente.get().setCpf(clienteForm.getCpf() == null ? cliente.get().getCpf() : clienteForm.getCpf());
            cliente.get().setDataNascimento(clienteForm.getDataNascimento() == null ? cliente.get().getDataNascimento() : clienteForm.getDataNascimento());
            cliente.get().setEmail(clienteForm.getEmail() == null ? cliente.get().getEmail() : clienteForm.getEmail());
            cliente.get().setDataAtualizacao(LocalDateTime.now());
            clienteRepository.save(cliente.get());
        }
        List<Endereco> endereco = enderecoRepository.findByClienteId(clienteId);
        if(form.getEndereco() != null) {
            EnderecoView enderecoForm = form.getEndereco();
            endereco.get(0).setRua(enderecoForm.getRua() == null ? endereco.get(0).getRua() : enderecoForm.getRua());
            endereco.get(0).setCep(enderecoForm.getCep() == null ? endereco.get(0).getCep() : enderecoForm.getCep());
            endereco.get(0).setBairro(enderecoForm.getBairro() == null ? endereco.get(0).getBairro() : enderecoForm.getBairro());
            endereco.get(0).setComplemento(enderecoForm.getComplemento() == null ? endereco.get(0).getComplemento() : enderecoForm.getComplemento());
            endereco.get(0).setCidade(enderecoForm.getCidade() == null ? endereco.get(0).getCidade() : enderecoForm.getCidade());
            endereco.get(0).setEstado(enderecoForm.getEstado() == null ? endereco.get(0).getEstado() : enderecoForm.getEstado());
            endereco.get(0).setDataAtualizacao(LocalDateTime.now());
            enderecoRepository.save(endereco.get(0));
        }
        List<Documento> documento = documentoRepository.findByClienteId(clienteId);
        if(form.getDocumento() != null) {
            DocumentoView documentoView = form.getDocumento();
            documento.get(0).setDocumento(documentoView.getDocumento() == null ? documento.get(0).getDocumento() : documentoView.getDocumento());
            documento.get(0).setDataAtualizacao(LocalDateTime.now());
            documentoRepository.save(documento.get(0));
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
        headers.add(HttpHeaders.LOCATION, "/cadastro/aceite" + proposta.get().getId());

        return new ResponseEntity<>(view, headers, HttpStatus.OK);
    }

    private void validaPassos(Long propostaId) throws ProposalNotFoundException, MissedStepException {
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
    }
}