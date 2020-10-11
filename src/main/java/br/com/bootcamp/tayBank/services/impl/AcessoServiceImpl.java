package br.com.bootcamp.tayBank.services.impl;

import br.com.bootcamp.tayBank.enums.StatusAcessoEnum;
import br.com.bootcamp.tayBank.exceptions.ServiceException;
import br.com.bootcamp.tayBank.forms.AtivaUsuarioForm;
import br.com.bootcamp.tayBank.forms.CriaTokenForm;
import br.com.bootcamp.tayBank.models.Acesso;
import br.com.bootcamp.tayBank.models.Cliente;
import br.com.bootcamp.tayBank.repositories.AcessoRepository;
import br.com.bootcamp.tayBank.repositories.ClienteRepository;
import br.com.bootcamp.tayBank.services.AcessoService;
import br.com.bootcamp.tayBank.utils.SendEmailUtils;
import br.com.bootcamp.tayBank.views.TokenView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AcessoServiceImpl implements AcessoService {

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    AcessoRepository acessoRepository;

    @Override
    public ResponseEntity<TokenView> token(CriaTokenForm form) throws ServiceException {
        Cliente cliente = clienteRepository.findByCpfAndEmail(form.getCpf(), form.getEmail());
        if(cliente == null) {
            throw new ServiceException("CPF ou email inválidos.");
        }

        String token = String.format("%05d", new Random().nextInt(100000));

        Optional<Acesso> acesso1 = acessoRepository.findById(cliente.getAcessoId());

        if(acesso1.isEmpty()) {
            Acesso acesso = new Acesso();
            acesso.setClienteId(cliente.getId());
            acesso.setTokenValidacao(token);
            acesso.setValidadeTokenValidacao(LocalDateTime.now().plusMinutes(10L));
            acesso.setStatus(StatusAcessoEnum.AGUARDANDO_ATIVACAO);
            acessoRepository.save(acesso);

            cliente.setAcessoId(acesso.getId());
            acessoRepository.save(acesso);
        } else if(acesso1.get().getStatus().equals(StatusAcessoEnum.AGUARDANDO_ATIVACAO)){
            acesso1.get().setTokenValidacao(token);
            acesso1.get().setValidadeTokenValidacao(LocalDateTime.now().plusMinutes(10L));
            acessoRepository.save(acesso1.get());
        } else {
            throw new ServiceException("Senha ja criada");
        }

        String para = cliente.getEmail();
        String assunto = "Token de acesso - Banco Digital TayBank";
        String mensagem = "Olá, tudo bem?\n\n" +
                "Seu token de acesso é: " +token +"\n\n" +
                "Cordialmente,\n" +
                "Equipe TayBank";

        SendEmailUtils.SendEmail(para, assunto, mensagem);

        TokenView tokenView = new TokenView();
        tokenView.setMensagem("Token de acesso enviado para o email " + form.getEmail());

        return new ResponseEntity<>(tokenView, HttpStatus.OK);
    }

    public void ativaUsuario(AtivaUsuarioForm form) throws ServiceException {
        Cliente cliente = clienteRepository.findByCpf(form.getCpf());
        if(cliente == null) {
            throw new ServiceException("Cliente não encontrado.");
        }

        if(!form.getSenha().equals(form.getConfirmaSenha())) {
            throw new ServiceException("Senhas não conferem.");
        }

        Optional<Acesso> acesso = acessoRepository.findById(cliente.getAcessoId());

        if(acesso.isEmpty()) {
            throw new ServiceException("Token ainda não gerado");
        }
        validaToken(form, acesso.get());

        acesso.get().setSenha(new BCryptPasswordEncoder().encode(form.getSenha()));
        acesso.get().setStatus(StatusAcessoEnum.ATIVO);
        acessoRepository.save(acesso.get());

        String para = cliente.getEmail();
        String assunto = "Token de acesso - Banco Digital TayBank";
        String mensagem = "Olá, tudo bem?\n\n" +
                "Sua senha foi modificada!\n\n" +
                "Cordialmente,\n" +
                "Equipe TayBank";

        SendEmailUtils.SendEmail(para, assunto, mensagem);
    }

    private void validaToken(AtivaUsuarioForm form, Acesso acesso) throws ServiceException {
        if(!acesso.getTokenValidacao().equals(form.getToken())){
            throw new ServiceException("Token inválido");
        }
        if(acesso.getValidadeTokenValidacao().isBefore(LocalDateTime.now())) {
            throw new ServiceException("Token expirado");
        }
    }
}