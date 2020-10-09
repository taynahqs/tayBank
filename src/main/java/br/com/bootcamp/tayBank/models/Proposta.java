package br.com.bootcamp.tayBank.models;

import br.com.bootcamp.tayBank.enums.StatusPropostaEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Proposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clienteId;

    private Long enderecoId;

    private Long documentoId;

    private StatusPropostaEnum status;

    private LocalDateTime dataCadastro;

    private LocalDateTime dataAtualizacao;
}
