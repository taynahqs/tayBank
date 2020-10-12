package br.com.bootcamp.tayBank.repositories;

import br.com.bootcamp.tayBank.model.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {

    Transferencia findByCodigoTransferencia(String codigoTransferencia);
}
