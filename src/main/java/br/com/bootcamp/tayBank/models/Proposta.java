package br.com.bootcamp.tayBank.models;

import br.com.bootcamp.tayBank.converters.StatusPropostaConverter;
import br.com.bootcamp.tayBank.enums.StatusPropostaEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    private String cpfCliente;

    private Long clienteId;

    private Long enderecoId;

    private Long documentoId;

    @Convert(converter = StatusPropostaConverter.class)
    private StatusPropostaEnum status;

    private LocalDateTime dataCadastro;

    private LocalDateTime dataAtualizacao;
}
