package br.com.bootcamp.tayBank.repositories;

import br.com.bootcamp.tayBank.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByCpf(String cpf);

    List<Cliente> findByEmail(String email);
}
