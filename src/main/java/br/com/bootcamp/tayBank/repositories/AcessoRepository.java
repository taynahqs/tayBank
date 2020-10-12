package br.com.bootcamp.tayBank.repositories;

import br.com.bootcamp.tayBank.model.Acesso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcessoRepository extends JpaRepository<Acesso, Long> {
}
