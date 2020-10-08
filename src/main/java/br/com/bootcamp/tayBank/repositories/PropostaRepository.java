package br.com.bootcamp.tayBank.repositories;

import br.com.bootcamp.tayBank.models.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PropostaRepository extends JpaRepository<Proposta, Long> {
    List<Proposta> findByClienteId(Long clienteId);
}
