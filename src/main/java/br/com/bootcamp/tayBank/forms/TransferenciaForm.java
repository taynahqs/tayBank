package br.com.bootcamp.tayBank.forms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransferenciaForm {
    @NotNull(message = "Informe o valor")
    private BigDecimal valor;

    @NotNull(message = "Informe a data da transferência")
    private LocalDateTime dataTransferencia;

    @NotNull(message = "Informe o documento identificador de origem")
    private String documentoIdentificadorOrigem;

    @NotNull(message = "Informe o codigo do banco de origem")
    private String codigoBancoOrigem;

    @NotNull(message = "Informe a conta de origem")
    private String contaOrigem;

    @NotNull(message = "Informe a agencia de origem")
    private String agenciaOrigem;

    @NotNull(message = "Informe o codigo da transferencia")
    private String codigoTransferencia;

    @NotNull(message = "Informe a conta de destino")
    private String contaDestino;

    @NotNull(message = "Informe a agencia de destino")
    private String agenciaDestino;
}