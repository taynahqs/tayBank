package br.com.bootcamp.tayBank.repositories;

import br.com.bootcamp.tayBank.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findByCpf(String cpf);

    Cliente findByEmail(String email);

    Cliente findByCpfAndEmail(String cpf, String email);
}
