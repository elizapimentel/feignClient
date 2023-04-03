package com.eliza.feignClient.services;

import com.eliza.feignClient.client.BacenClient;
import com.eliza.feignClient.mapper.TaxaJurosMapper;
import com.eliza.feignClient.model.TaxaJurosRequest;
import com.eliza.feignClient.model.dto.TaxaJurosResponse;
import com.eliza.feignClient.repositories.TaxaJurosRepository;
import com.eliza.feignClient.services.exceptions.TaxaJurosNotFoundException;
import com.eliza.feignClient.utils.CNPJValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.eliza.feignClient.utils.constants.Messages.OBJECT_NOT_FOUND_MESSAGE;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class TaxaJurosServiceImpl implements TaxaJurosService {
    @Autowired
    private BacenClient client;
    @Autowired
    private TaxaJurosRepository repo;
    @Autowired
    private TaxaJurosMapper mapper;

    @Override
    public List<TaxaJurosRequest> getAllTaxasBacen(int qtd) {
        List<TaxaJurosResponse> response = client.getAllTaxasBacen(qtd).getValue();
        List<TaxaJurosRequest> requestList = response.stream()
                .map(taxaReq -> TaxaJurosMapper.MAPPER
                        .dtoToModel(taxaReq))
                .collect(Collectors.toList());
        return repo.saveAll(requestList);
    }

    @Override
    public List<TaxaJurosResponse> getAllTaxasBD() {
        List<TaxaJurosRequest> model = repo.findAll();
        return model.stream()
                .map(taxa -> TaxaJurosMapper.MAPPER.modelToDto(taxa))
                .collect(Collectors.toList());
    }

  @Override
    public TaxaJurosResponse getTaxasById(Long id) {
      Optional<TaxaJurosRequest> taxa = repo.findById(id);
      return TaxaJurosMapper.MAPPER.modelToDto(taxa.orElseThrow(
              () -> new TaxaJurosNotFoundException(format(OBJECT_NOT_FOUND_MESSAGE, id))
      ));
  }

    @Override
    public TaxaJurosResponse postNovaTaxa(TaxaJurosResponse txJuros) {
        CNPJValidator validator = new CNPJValidator();
        TaxaJurosRequest novaTaxa = mapper.dtoToModel(txJuros);
        TaxaJurosRequest salvaModel = repo.save(novaTaxa);
        TaxaJurosResponse salvaDto = TaxaJurosMapper.MAPPER.modelToDto(salvaModel);
        validator.assertValid(salvaDto.getCnpj8());
        return salvaDto;

    }

    @Override
    public TaxaJurosResponse updateTaxa(TaxaJurosResponse txJuros) {
        CNPJValidator validator = new CNPJValidator();
        getTaxasById(txJuros.getId());
        TaxaJurosRequest atualizaTaxa = mapper.dtoToModel(txJuros);
        TaxaJurosRequest salvaNovaTaxa = repo.save(atualizaTaxa);
        TaxaJurosResponse atualizarDto = TaxaJurosMapper.MAPPER.modelToDto(salvaNovaTaxa);
        validator.assertValid(atualizarDto.getCnpj8());
        return atualizarDto;
    }

    @Override
    public void deleteTaxa(Long id) {
        getTaxasById(id);
        repo.deleteById(id);
    }

    @Override
    public List<TaxaJurosResponse> getTaxasPorAnoMes(String anoMes) {
        if (!anoMes.matches("\\d{4}-\\d{2}")) throw new TaxaJurosNotFoundException
                    ("O parâmetro 'anoMes' deve seguir o formato de números: yyyy-mm");
        if (anoMes != null) {
            List<TaxaJurosRequest> modelInfo = repo.buscarAnoMes(anoMes);
            return modelInfo.stream().map(am -> TaxaJurosMapper
                    .MAPPER.modelToDto(am)).collect(Collectors.toList());

        }
        List<TaxaJurosRequest> taxas = repo.findAll();
        List<TaxaJurosResponse> taxasResponse = taxas.stream().map(am -> TaxaJurosMapper.MAPPER.modelToDto(am))
                .collect(Collectors.toList());
        return taxasResponse;
    }

    @Override
    public Page<TaxaJurosResponse> getTaxasPorPagina(int pag, int tam) {
        if (tam == 0) throw new TaxaJurosNotFoundException("Campo 'tam' não pode ser igual ou menor que 0");
        PageRequest pageRequest = PageRequest.of(pag, tam);
        Page<TaxaJurosRequest> modelPag = repo.buscarPorPg(pageRequest);
        return modelPag.map(p -> TaxaJurosMapper.MAPPER.modelToDto(p));

    }


}
