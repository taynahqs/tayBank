package br.com.bootcamp.tayBank.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal valor;

    private LocalDateTime dataTransferencia;

    private String documentoIdentificadorOrigem;

    private String codigoBancoOrigem;

    private String contaOrigem;

    private String agenciaOrigem;

    private String codigoTransferencia;

    private String contaDestino;

    private String agenciaDestino;

    private LocalDateTime dataCadastro;

    private LocalDateTime dataAtualizacao;
}
