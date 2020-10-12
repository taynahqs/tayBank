package br.com.bootcamp.tayBank.services.impl;

import br.com.bootcamp.tayBank.exceptions.ServiceException;
import br.com.bootcamp.tayBank.forms.TransferenciaForm;
import br.com.bootcamp.tayBank.model.Conta;
import br.com.bootcamp.tayBank.model.Transferencia;
import br.com.bootcamp.tayBank.repositories.ContaRepository;
import br.com.bootcamp.tayBank.repositories.TransferenciaRepository;
import br.com.bootcamp.tayBank.services.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class TransacaoServiceImpl implements TransacaoService {

    @Autowired
    ContaRepository contaRepository;

    @Autowired
    TransferenciaRepository transferenciaRepository;

    @Async
    public void recebeTransferencia(TransferenciaForm form) throws ServiceException{
        Conta conta = contaRepository.findByCodigoContaAndAgencia(form.getContaDestino(), form.getAgenciaDestino());
        if(conta == null) {
            throw new ServiceException("Conta não encontrada");
        }

        BigDecimal saldoAnterior = conta.getSaldo();
        BigDecimal saldoNovo = saldoAnterior.add(form.getValor());

        conta.setSaldo(saldoNovo);
        conta.setDataAtualizacao(LocalDateTime.now());
        contaRepository.save(conta);

        Transferencia transferencia1 = transferenciaRepository.findByCodigoTransferencia(form.getCodigoTransferencia());
        if(transferencia1 != null) {
            throw new ServiceException("Tranferencia com o codigo " +form.getCodigoTransferencia() +" já realizada");
        }

        Transferencia transferencia = new Transferencia();
        transferencia.setValor(form.getValor());
        transferencia.setAgenciaOrigem(form.getAgenciaOrigem());
        transferencia.setCodigoBancoOrigem(form.getCodigoBancoOrigem());
        transferencia.setContaOrigem(form.getContaOrigem());
        transferencia.setCodigoTransferencia(form.getCodigoTransferencia());
        transferencia.setDocumentoIdentificadorOrigem(form.getDocumentoIdentificadorOrigem());
        transferencia.setAgenciaDestino(form.getAgenciaDestino());
        transferencia.setContaDestino(form.getContaDestino());
        transferencia.setDataTransferencia(form.getDataTransferencia());
        transferencia.setDataCadastro(form.getDataTransferencia());
        transferencia.setDataAtualizacao(LocalDateTime.now());
        transferenciaRepository.save(transferencia);

    }
}
