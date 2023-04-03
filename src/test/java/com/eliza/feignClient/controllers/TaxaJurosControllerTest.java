
package com.eliza.feignClient.controllers;

import com.eliza.feignClient.mapper.TaxaJurosMapper;
import com.eliza.feignClient.model.TaxaJurosRequest;
import com.eliza.feignClient.model.dto.TaxaJurosResponse;
import com.eliza.feignClient.services.TaxaJurosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.eliza.feignClient.services.TaxaJurosServiceImplTest.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@WebMvcTest(controllers = TaxaJurosController.class)
public class TaxaJurosControllerTest {

    @MockBean
    private TaxaJurosService service;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveRetornarItensTrazidosDoBacen() throws Exception {
        TaxaJurosRequest bacen = new TaxaJurosRequest();
        when(service.getAllTaxasBacen(1)).thenReturn(List.of(bacen));
        this.mockMvc.perform(get("/taxasMes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void deveRetornarTodosOsItensSalvosNoBDOk() throws Exception {

        TaxaJurosRequest taxas = new TaxaJurosRequest();
        taxas.setMes("Jan-2023");
        taxas.setModalidade("FINANCIAMENTO IMOBILIÁRIO COM TAXAS REGULADAS - PRÉ-FIXADO 1");
        taxas.setPosicao(1);
        taxas.setInstituicaoFinanceira("BCO DO BRASIL");
        taxas.setTaxaJurosAoMes(0.0);
        taxas.setTaxaJurosAoAno(0.0);
        taxas.setCnpj8("26637142000158");
        taxas.setAnoMes("2023-01");

        TaxaJurosResponse taxasDto = TaxaJurosMapper.MAPPER.modelToDto(taxas);

        when(service.getAllTaxasBD()).thenReturn(List.of(taxasDto));

        this.mockMvc.perform(get("/taxasMes/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void deveRetornarItemPorId() throws Exception {
        TaxaJurosResponse dto = new TaxaJurosResponse();
        when(service.getTaxasById(anyLong())).thenReturn(dto);
        this.mockMvc.perform(get("/taxasMes/" + ID)
                        .content(asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deveRetornarError400DoCatchBuscaPorId() throws Exception {
        TaxaJurosResponse dto = new TaxaJurosResponse();
        when(service.getTaxasById(anyLong())).thenThrow(Error.class);
        this.mockMvc.perform(get("/taxasMes/" + ID)
                        .content(asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deveAtualizarPorIdOk() throws Exception {
        TaxaJurosRequest taxaJurosRequest = new TaxaJurosRequest(ID, MES, MODALIDADE, POSICAO, INSTITUICAO_FINANCEIRA, TAXA_JUROS_AO_MES,
                TAXA_JUROS_AO_ANO, CNPJ_8, ANO_MES);
        TaxaJurosResponse taxaJurosResponse = TaxaJurosMapper.MAPPER.modelToDto(taxaJurosRequest);

        when(service.getTaxasById(anyLong()))
                .thenReturn(taxaJurosResponse);
        when(service.updateTaxa(any(TaxaJurosResponse.class)))
                .thenReturn(taxaJurosResponse);

        this.mockMvc.perform(put("/taxasMes/" + ID)
                        .content(asJsonString(taxaJurosResponse))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Mes").value(taxaJurosResponse.getMes()))
                .andExpect(jsonPath("$.Modalidade").value(taxaJurosResponse.getModalidade()))
                .andExpect(jsonPath("$.Posicao").value(taxaJurosResponse.getPosicao()))
                .andExpect(jsonPath("$.InstituicaoFinanceira").value(taxaJurosResponse.getInstituicaoFinanceira()))
                .andExpect(jsonPath("$.TaxaJurosAoMes").value(taxaJurosResponse.getTaxaJurosAoMes()))
                .andExpect(jsonPath("$.TaxaJurosAoAno").value(taxaJurosResponse.getTaxaJurosAoAno()))
                .andExpect(jsonPath("$.cnpj8").value(taxaJurosResponse.getCnpj8()))
                .andExpect(jsonPath("$.anoMes").value(taxaJurosResponse.getAnoMes()));
    }

    @Test
    public void deveRetornarErro400DoCatchAtualizarPorId() throws Exception {
        TaxaJurosResponse dto = new TaxaJurosResponse(ID, MES, MODALIDADE, POSICAO, INSTITUICAO_FINANCEIRA, TAXA_JUROS_AO_MES,
                TAXA_JUROS_AO_ANO, CNPJ_8, ANO_MES);
        when(service.getTaxasById(anyLong())).thenThrow(Error.class);
        doThrow(Error.class).when(service).updateTaxa(any());
        this.mockMvc.perform(put("/taxasMes/" + ID)
                        .content(asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void deveRetornarNovoItemCriadoOk() throws Exception {
        TaxaJurosResponse dto = new TaxaJurosResponse(ID, MES, MODALIDADE, POSICAO, INSTITUICAO_FINANCEIRA, TAXA_JUROS_AO_MES,
                TAXA_JUROS_AO_ANO, CNPJ_8, ANO_MES);
        when(service.postNovaTaxa(any(TaxaJurosResponse.class)))
                .thenReturn(dto);
        this.mockMvc.perform(post("/taxasMes/novo")
                        .content(asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andReturn();
    }

    @Test
    public void deveRetornarErro422DoCatchCriaNovoItem() throws Exception {
        TaxaJurosResponse dto = new TaxaJurosResponse(ID, MES, MODALIDADE, POSICAO, INSTITUICAO_FINANCEIRA, TAXA_JUROS_AO_MES,
                TAXA_JUROS_AO_ANO, CNPJ_8, ANO_MES);
        doThrow(Error.class).when(service).postNovaTaxa(any());
        this.mockMvc.perform(post("/taxasMes/novo")
                        .content(asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    public void deveDeletarItemPorIdOk() throws Exception {
        TaxaJurosResponse response = new TaxaJurosResponse(ID, MES, MODALIDADE, POSICAO, INSTITUICAO_FINANCEIRA, TAXA_JUROS_AO_MES,
                TAXA_JUROS_AO_ANO, CNPJ_8, ANO_MES);

        when(service.getTaxasById(anyLong())).thenReturn(response);
        doNothing().when(service).deleteTaxa(response.getId());

        this.mockMvc.perform(delete("/taxasMes/" + 1L))
                .andExpect(status().isOk());
        verify(service, times(1)).deleteTaxa(1L);
    }

    @Test
    public void deveRetornarBuscaPorAnoMesOk() throws Exception {
        TaxaJurosResponse response = new TaxaJurosResponse();
        when(service.getTaxasPorAnoMes(anyString())).thenReturn(List.of(response));
        this.mockMvc.perform(get("/taxasMes/anoMes").param("anoMes", ANO_MES)
                        .content(asJsonString(response))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deveRetornarFiltrarPorPaginaOk() throws Exception {
        TaxaJurosResponse txResponse = new TaxaJurosResponse();
        List<TaxaJurosResponse> responseList = new ArrayList<>(List.of(txResponse));
        Pageable pageable = PageRequest.of(0, 1);
        Page<TaxaJurosResponse> response = new PageImpl<>(responseList, pageable, responseList.size());
        when(service.getTaxasPorPagina(anyInt(), anyInt())).thenReturn(response);
        this.mockMvc.perform(get("/taxasMes/filtrar")
                        .param("pag", "0")
                        .param("tam", "1")
                        .content(asJsonString(response))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
