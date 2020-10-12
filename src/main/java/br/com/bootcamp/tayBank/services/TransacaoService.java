package br.com.bootcamp.tayBank.services;

import br.com.bootcamp.tayBank.exceptions.ServiceException;
import br.com.bootcamp.tayBank.forms.TransferenciaForm;

public interface TransacaoService {
    void recebeTransferencia(TransferenciaForm transferenciaForm) throws ServiceException;
}
