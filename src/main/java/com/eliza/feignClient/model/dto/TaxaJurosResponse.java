package com.eliza.feignClient.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaxaJurosResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty("Mes")
    @NotNull
    private String mes;

    @JsonProperty("Modalidade")
    @NotNull
    private String modalidade;

    @JsonProperty("Posicao")
    private Integer posicao;

    @JsonProperty("InstituicaoFinanceira")
    @NotNull
    private String instituicaoFinanceira;

    @JsonProperty("TaxaJurosAoMes")
    @NotNull
    private Double taxaJurosAoMes;

    @JsonProperty("TaxaJurosAoAno")
    private Double taxaJurosAoAno;

    @CNPJ
    @NotNull
    @JsonProperty("cnpj8")
    private String cnpj8;

    @JsonProperty("anoMes")
    @NotNull
    private String anoMes;
}
