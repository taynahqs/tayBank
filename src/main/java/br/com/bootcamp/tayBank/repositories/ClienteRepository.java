package br.com.bootcamp.tayBank.repositories;

import br.com.bootcamp.tayBank.entities.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {

    ClienteEntity findByCpf(String cpf);

    List<ClienteEntity> findByEmail(String email);
}
