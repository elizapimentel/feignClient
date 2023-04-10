package com.eliza.feignClient.integration;

import com.eliza.feignClient.model.TaxaJurosRequest;
import com.eliza.feignClient.model.dto.TaxaJurosResponse;
import com.eliza.feignClient.repositories.TaxaJurosRepository;
import com.eliza.feignClient.wrapper.PaginatedResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.eliza.feignClient.services.TaxaJurosServiceImplTest.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaxaJurosControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private TaxaJurosRepository repo;
    TaxaJurosRequest taxasModel;
    TaxaJurosResponse taxasDto;

    @BeforeEach
    public void setUp() {
        this.taxasModel = new TaxaJurosRequest();
        gerarTaxas();
    }

    @AfterEach
    public void cleanUp() {
        repo.deleteAll();
    }

    @Test
    void deveCriarNovaTaxaOk() {
        HttpEntity<TaxaJurosResponse> httpEntity = new HttpEntity<>(taxasDto);
        ResponseEntity<TaxaJurosRequest> response = this.testRestTemplate
                .exchange("/taxasMes/novo", HttpMethod.POST, httpEntity, TaxaJurosRequest.class);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertEquals(response.getBody().getMes(), MES);
        assertEquals(response.getBody().getModalidade(), MODALIDADE);
        assertEquals(response.getBody().getPosicao(), POSICAO);
        assertEquals(response.getBody().getInstituicaoFinanceira(), INSTITUICAO_FINANCEIRA);
        assertEquals(response.getBody().getTaxaJurosAoMes(), TAXA_JUROS_AO_MES);
        assertEquals(response.getBody().getTaxaJurosAoAno(), TAXA_JUROS_AO_ANO);
        assertEquals(response.getBody().getCnpj8(), CNPJ_8);
        assertEquals(response.getBody().getAnoMes(), ANO_MES);
    }

    @Test
    void deveRetornarTodasTaxasDoBDOk() {
        ResponseEntity<TaxaJurosResponse[]> response = this.testRestTemplate
                .exchange("/taxasMes/", HttpMethod.GET, null, TaxaJurosResponse[].class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void deveRetornarTaxaMesPorIdOk() {
        TaxaJurosRequest taxas = this.repo.save(this.taxasModel);
        ResponseEntity<TaxaJurosResponse> response = this.testRestTemplate
                .exchange("/taxasMes/" + taxas.getId(), HttpMethod.GET, null, TaxaJurosResponse.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void deveAtualizarDadosTaxaMesOk() {
        TaxaJurosRequest taxas = this.repo.save(this.taxasModel);
        TaxaJurosResponse taxaDto = new TaxaJurosResponse(ID, MES, MODALIDADE, 2, INSTITUICAO_FINANCEIRA,
                TAXA_JUROS_AO_MES, TAXA_JUROS_AO_ANO, CNPJ_8, ANO_MES);
        HttpEntity<TaxaJurosResponse> httpEntity = new HttpEntity<>(taxaDto);
        ResponseEntity<TaxaJurosResponse> response = this.testRestTemplate
                .exchange("/taxasMes/" + taxas.getId(), HttpMethod.PUT, httpEntity, TaxaJurosResponse.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void deveDeletarDadosTaxaMesOk() {
        TaxaJurosRequest taxas = this.repo.save(this.taxasModel);
        ResponseEntity<Void> response = this.testRestTemplate
                .exchange("/taxasMes/" + taxas.getId(), HttpMethod.DELETE, null, Void.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void deverRetornarTaxaMesPorAnoMesOk() {
        ResponseEntity<TaxaJurosResponse[]> response = this.testRestTemplate
                .exchange("/taxasMes/anoMes?anoMes=" + ANO_MES, HttpMethod.GET, null,
                        TaxaJurosResponse[].class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void deverRetornarTaxaPorPaginaOk() {
        ParameterizedTypeReference<PaginatedResponse<TaxaJurosResponse>> responseType =
                new ParameterizedTypeReference<>() { };
        ResponseEntity<PaginatedResponse<TaxaJurosResponse>> response = this.testRestTemplate
                .exchange("/taxasMes/filtrar?pag=0&tam=1", HttpMethod.GET, null,
                        responseType);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response);
    }

    private void gerarTaxas() {
        taxasModel = new TaxaJurosRequest(ID, MES, MODALIDADE, POSICAO, INSTITUICAO_FINANCEIRA,
                TAXA_JUROS_AO_MES, TAXA_JUROS_AO_ANO, CNPJ_8, ANO_MES);
        taxasDto = new TaxaJurosResponse(ID, MES, MODALIDADE, POSICAO, INSTITUICAO_FINANCEIRA,
                TAXA_JUROS_AO_MES, TAXA_JUROS_AO_ANO, CNPJ_8, ANO_MES);

    }
}
