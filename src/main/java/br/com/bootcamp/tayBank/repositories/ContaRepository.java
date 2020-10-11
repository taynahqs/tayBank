package br.com.bootcamp.tayBank.repositories;

import br.com.bootcamp.tayBank.models.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<Conta, Long> {
}
