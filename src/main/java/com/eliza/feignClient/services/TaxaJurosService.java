package com.eliza.feignClient.services;

import com.eliza.feignClient.model.TaxaJurosRequest;
import com.eliza.feignClient.model.dto.TaxaJurosResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaxaJurosService {
    List<TaxaJurosRequest> getAllTaxasBacen(int qtd);
    List<TaxaJurosResponse> getAllTaxasBD();
    TaxaJurosResponse getTaxasById(Long id);
    TaxaJurosResponse postNovaTaxa(TaxaJurosResponse txJuros);
    TaxaJurosResponse updateTaxa(TaxaJurosResponse txJuros);
    void deleteTaxa(Long id);
    List<TaxaJurosResponse> getTaxasPorAnoMes(String anoMes);
    Page<TaxaJurosResponse> getTaxasPorPagina(int pag, int tam);

}
