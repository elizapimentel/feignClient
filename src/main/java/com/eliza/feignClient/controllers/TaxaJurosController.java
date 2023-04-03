package com.eliza.feignClient.controllers;

import com.eliza.feignClient.model.TaxaJurosRequest;
import com.eliza.feignClient.model.dto.TaxaJurosResponse;
import com.eliza.feignClient.services.TaxaJurosService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/taxasMes")
public class TaxaJurosController {

    @Autowired
    private TaxaJurosService taxaJurosService;

    //pegar os dados do Bacen
    @GetMapping()
    public ResponseEntity<List<TaxaJurosRequest>> getAllTaxaBacen(
            @RequestParam(value = "retornar", defaultValue = "100") Integer qtd) {
        List<TaxaJurosRequest> taxasMes = taxaJurosService.getAllTaxasBacen(qtd);
        return ResponseEntity.status(200).body(taxasMes);
    }

    //trazer os dados já salvos no BD
    @GetMapping("/")
    public ResponseEntity<List<TaxaJurosResponse>> getAllTxMes() {
        List<TaxaJurosResponse> taxaMes = taxaJurosService.getAllTaxasBD();
        return ResponseEntity.status(200).body(taxaMes);
    }

    //buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<TaxaJurosResponse> buscarTaxaPorId(
            @PathVariable(value = "id") Long id) {
        try {
            TaxaJurosResponse txBacen = taxaJurosService.getTaxasById(id);
            return ResponseEntity.status(200).body(txBacen);
        } catch (Error e) {
            return ResponseEntity.status(400).build();
        }
    }

    //criar novo registro
    @PostMapping("/novo")
    //@Valid para validar os itens NotNull
    public ResponseEntity<?> cadastrar(@Valid @RequestBody TaxaJurosResponse txJuros) {
        try {
            return ResponseEntity.status(201).body(taxaJurosService.postNovaTaxa(txJuros));
        } catch (Error e) {
            return ResponseEntity.status(422).build();
        }
    }

    //atualizar registro
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody TaxaJurosResponse txBacen) {
        try {
            TaxaJurosResponse taxaAtual = taxaJurosService.getTaxasById(id);
            BeanUtils.copyProperties(txBacen, taxaAtual, "id");
            return ResponseEntity.status(200).body(taxaJurosService.updateTaxa(taxaAtual));
        } catch (Error e) {
            return ResponseEntity.status(400).build();
        }
    }

    //deletar registro
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        TaxaJurosResponse txBacen = taxaJurosService.getTaxasById(id);
        taxaJurosService.deleteTaxa(txBacen.getId());
        ResponseEntity.status(200);
    }

    //buscar por anoMes - query
    @GetMapping("/anoMes")
    public ResponseEntity<List<TaxaJurosResponse>> buscarPorAnoMes(@RequestParam("anoMes") String anoMes) {
        List<TaxaJurosResponse> listarAnoMes = taxaJurosService.getTaxasPorAnoMes(anoMes);
        return ResponseEntity.status(200).body(listarAnoMes);
    }

    //buscar por pagina e quantidade de itens a serem retornados - query
    @GetMapping("/filtrar")
    public ResponseEntity<Page<TaxaJurosResponse>> buscarPorPg(@RequestParam(value = "pag") int pag, @RequestParam(value = "tam") int tam) {
        return ResponseEntity.status(200).body(taxaJurosService.getTaxasPorPagina(pag, tam));
    }
}

