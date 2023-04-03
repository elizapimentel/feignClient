package com.eliza.feignClient.feignClient;


import com.eliza.feignClient.client.BacenClient;
import com.eliza.feignClient.model.dto.TaxaJurosResponse;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.mock.HttpMethod;
import feign.mock.MockClient;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.openfeign.support.SpringMvcContract;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BacenClientTest {


    private static String BASE_URL = "https://olinda.bcb.gov.br/olinda/servico/taxaJuros/versao/v2/odata/TaxasJurosMensalPorMes";
    private static String RESPONSE = "{\"@odata.context\": \"https://was-p.bcnet.bcb.gov.br/olinda/servico/taxaJuros/versao/v2/odata$metadata#TaxasJurosMensalPorMes\",\n" +
            "   \"value\": [{\n" +
            "   \"Mes\": \"Jan-2023\",\n" +
            "   \"Modalidade\": \"FINANCIAMENTO IMOBILIÁRIO COM TAXAS REGULADAS - PRÉ-FIXADO\",\n" +
            "   \"Posicao\": 1,\n" +
            "   \"InstituicaoFinanceira\": \"BCO DO BRASIL S.A.\",\n" +
            "   \"TaxaJurosAoMes\": 0.0,\n" +
            "   \"TaxaJurosAoAno\": 0.0,\n" +
            "   \"cnpj8\": \"00000000\",\n" +
            "   \"anoMes\": \"2023-01\"}]}";

    private BacenClient client;

    @Test
    public void whenGetAllClientThenReturnOk(){
        this.builderFeignClient(new MockClient().ok(
                HttpMethod.GET,
                BASE_URL.concat("?$top=2&$format=json"),
                RESPONSE
        ));

        List<TaxaJurosResponse> listClient = client.getAllTaxasBacen(2).getValue();

        assertThat(listClient).isNotEmpty();
        assertThat(listClient).hasSize(1);
    }

    private void builderFeignClient(MockClient mockClient){
        client = Feign.builder()
                .client(mockClient)
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .contract(new SpringMvcContract())
                .target(BacenClient.class, BASE_URL);
    }

}
