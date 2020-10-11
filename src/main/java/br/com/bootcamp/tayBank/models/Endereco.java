package br.com.bootcamp.tayBank.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clienteId;

    private String cep;

    private String rua;

    private String bairro;

    private String complemento;

    private String cidade;

    private String estado;

    private LocalDateTime dataCadastro;

    private LocalDateTime dataAtualizacao;
}
