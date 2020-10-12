package br.com.bootcamp.tayBank.scheduler.service;

import br.com.bootcamp.tayBank.enums.StatusPropostaEnum;
import br.com.bootcamp.tayBank.model.Cliente;
import br.com.bootcamp.tayBank.model.Conta;
import br.com.bootcamp.tayBank.model.Proposta;
import br.com.bootcamp.tayBank.repositories.ClienteRepository;
import br.com.bootcamp.tayBank.repositories.ContaRepository;
import br.com.bootcamp.tayBank.repositories.PropostaRepository;
import br.com.bootcamp.tayBank.utils.SendEmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ContaService {

    @Autowired
    PropostaRepository propostaRepository;

    @Autowired
    ContaRepository contaRepository;

    @Autowired
    ClienteRepository clienteRepository;

    public void criaConta() {
        List<Proposta> propostas = propostaRepository.findByStatus(StatusPropostaEnum.CRIACAO_CONTA);

        if(propostas != null && !propostas.isEmpty()) {
            for (Proposta proposta : propostas) {
                Conta conta = new Conta();
                conta.setCodigoConta(geraConta());
                conta.setAgencia(geraAgencia());
                conta.setPropostaId(proposta.getId());
                conta.setDataCadastro(LocalDateTime.now());
                conta.setDataAtualizacao(LocalDateTime.now());
                contaRepository.save(conta);

                proposta.setStatus(StatusPropostaEnum.CONTA_CRIADA);
                propostaRepository.save(proposta);

                Optional<Cliente> cliente = clienteRepository.findById(proposta.getClienteId());

                String para = cliente.get().getEmail();
                String assunto = "Dados da conta - Banco Digital TayBank";
                String mensagem = "Olá, tudo bem?\n\n" +
                        "Segue os dados de sua conta corrente:\n" +
                        "Conta: " + conta.getCodigoConta() + "\n" +
                        "Agencia: " + conta.getAgencia() + "\n" +
                        "Banco: " + conta.getCodigoBanco() + "\n\n" +
                        "Cordialmente,\n" +
                        "Equipe TayBank";

                SendEmailUtils.SendEmail(para, assunto, mensagem);
            }
        }
    }

    private String geraAgencia() {
        Random rnd = new Random();
        Integer number = rnd.nextInt(9999);
        String agencia = number.toString();
        if(agencia.charAt(0) == '0') {
            agencia = geraAgencia();
        }
        return agencia;
    }

    private String geraConta() {
        Random rnd = new Random();
        Integer number = rnd.nextInt(99999999);
        String conta = number.toString();
        if(conta.charAt(0) == '0') {
            conta = geraConta();
        }
        return conta;
    }
}
