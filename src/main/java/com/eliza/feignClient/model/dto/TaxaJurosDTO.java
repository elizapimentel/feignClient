package com.eliza.feignClient.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxaJurosDTO implements Serializable {

    @JsonProperty("value")
    private List<TaxaJurosResponse> value;
}
