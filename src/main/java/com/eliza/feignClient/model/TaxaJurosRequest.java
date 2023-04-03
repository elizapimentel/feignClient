package com.eliza.feignClient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Taxas_Mensal")
public class TaxaJurosRequest implements Serializable {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonProperty("id")
    @Id
    private Long id;
    @JsonProperty("Mes")
    private String mes;
    @JsonProperty("Modalidade")
    private String modalidade;
    @JsonProperty("Posicao")
    private Integer posicao;
    @JsonProperty("InstituicaoFinanceira")
    private String instituicaoFinanceira;
    @JsonProperty("TaxaJurosAoMes")
    private Double taxaJurosAoMes;
    @JsonProperty("TaxaJurosAoAno")
    private Double taxaJurosAoAno;
    @JsonProperty("cnpj8")
    private String cnpj8;
    @JsonProperty("anoMes")
    private String anoMes;
}
