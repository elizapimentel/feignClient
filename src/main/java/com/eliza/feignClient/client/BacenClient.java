package com.eliza.feignClient.client;

import com.eliza.feignClient.model.dto.TaxaJurosDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "bacen", url = "${api.bacen.url}")
public interface BacenClient {
    @GetMapping("?$top={max}&$format=json")
    TaxaJurosDTO getAllTaxasBacen(@PathVariable Integer max);
}
