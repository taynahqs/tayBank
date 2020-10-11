package br.com.bootcamp.tayBank.repositories;

import br.com.bootcamp.tayBank.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findByCpf(String cpf);

    Cliente findByEmail(String email);

    Cliente findByCpfAndEmail(String cpf, String email);
}
