package br.com.bootcamp.tayBank.model;

import br.com.bootcamp.tayBank.enums.StatusAcessoEnum;
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
public class Acesso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clienteId;

    private String tokenValidacao;

    private LocalDateTime validadeTokenValidacao;

    private String senha;

    private StatusAcessoEnum status;


}
