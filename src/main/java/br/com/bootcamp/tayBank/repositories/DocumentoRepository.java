package br.com.bootcamp.tayBank.repositories;

import br.com.bootcamp.tayBank.models.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    List<Documento> findByClienteId(Long clienteId);
}
