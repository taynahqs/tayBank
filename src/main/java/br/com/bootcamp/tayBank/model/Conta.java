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
@AllArgsConstructor
@NoArgsConstructor
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String agencia;

    private String codigoConta;

    private String codigoBanco = "123";

    private Long propostaId;

    private BigDecimal saldo = BigDecimal.ZERO;

    private LocalDateTime dataCadastro;

    private LocalDateTime dataAtualizacao;
}
