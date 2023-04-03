package com.eliza.feignClient.services;

import com.eliza.feignClient.client.BacenClient;
import com.eliza.feignClient.mapper.TaxaJurosMapper;
import com.eliza.feignClient.model.TaxaJurosRequest;
import com.eliza.feignClient.model.dto.TaxaJurosDTO;
import com.eliza.feignClient.model.dto.TaxaJurosResponse;
import com.eliza.feignClient.repositories.TaxaJurosRepository;
import com.eliza.feignClient.services.exceptions.TaxaJurosNotFoundException;
import com.eliza.feignClient.utils.CNPJValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TaxaJurosServiceImplTest {

    public static final Long ID                         = 1L;
    public static final String MES                      = "Jan-2023";
    public static final String MODALIDADE               = "FINANCIAMENTO IMOBILIÁRIO COM TAXAS REGULADAS - PRÉ-FIXADO";
    public static final Integer POSICAO                 = 1;
    public static final String INSTITUICAO_FINANCEIRA   = "BCO DO BRASIL S.A.";
    public static final Double TAXA_JUROS_AO_MES        = 0.0;
    public static final Double TAXA_JUROS_AO_ANO        = 0.0;
    public static final String CNPJ_8                   = "26637142000158";
    public static final String ANO_MES                  = "2023-01";
    private final String validString                    = "26.637.142/0001-58";

    @InjectMocks
    private TaxaJurosServiceImpl service;

    @Mock
    private TaxaJurosRepository repo;

    @Mock
    private BacenClient client;

    @Mock
    private TaxaJurosMapper mapper;

    private TaxaJurosRequest taxasModel;
    private TaxaJurosResponse taxasDto;
    private Optional<TaxaJurosRequest> optionalTaxas;
    private Optional<TaxaJurosResponse> optionalTaxasDto;
    private TaxaJurosDTO dto;
    private CNPJValidator validator;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); //chamas os mocks
        gerarTaxas(); //chama o metodo criado para gerar os dados
        validator = new CNPJValidator();

    }

    @Test
    void deveMapearRequestParaResponse() {
        TaxaJurosRequest model = taxasModel;

        TaxaJurosResponse dto = mapper.modelToDto(model);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(model.getId());
        assertThat(dto.getMes()).isEqualTo(model.getMes());
        assertThat(dto.getModalidade()).isEqualTo(model.getModalidade());
        assertThat(dto.getPosicao()).isEqualTo(model.getPosicao());
        assertThat(dto.getInstituicaoFinanceira()).isEqualTo(model.getInstituicaoFinanceira());
        assertThat(dto.getTaxaJurosAoMes()).isEqualTo(model.getTaxaJurosAoMes());
        assertThat(dto.getTaxaJurosAoAno()).isEqualTo(model.getTaxaJurosAoAno());
        assertThat(dto.getCnpj8()).isEqualTo(model.getCnpj8());
        assertThat(dto.getAnoMes()).isEqualTo(model.getAnoMes());
    }

    @Test
    void deveRetornarTodosOsItensDaApiExterna() {
        when(client.getAllTaxasBacen(1)).
                thenReturn(dto);
        when(repo.saveAll(any()))
                .thenReturn(List.of(taxasModel));
        service.getAllTaxasBacen(1);
        verify(client, times(1))
                .getAllTaxasBacen(1);
        assertThat(mapper.dtoToModel(taxasDto)).isEqualTo(taxasModel);

    }

    @Test
    void deveRetornarTodosOsDadosDaApiExternaSalvasNoBancoDeDadosOk() {
        when(repo.findAll()).thenReturn(List.of(taxasModel));

        List<TaxaJurosResponse> response = service.getAllTaxasBD();

        assertThat(mapper.modelToDto(taxasModel)).isEqualTo(taxasDto);
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(TaxaJurosResponse.class, response.get(0).getClass());
        assertEquals(ID, response.get(0).getId());
        assertEquals(MES, response.get(0).getMes());
        assertEquals(MODALIDADE, response.get(0).getModalidade());
        assertEquals(POSICAO, response.get(0).getPosicao());
        assertEquals(INSTITUICAO_FINANCEIRA, response.get(0).getInstituicaoFinanceira());
        assertEquals(TAXA_JUROS_AO_MES, response.get(0).getTaxaJurosAoMes());
        assertEquals(TAXA_JUROS_AO_ANO, response.get(0).getTaxaJurosAoAno());
        assertEquals(CNPJ_8, response.get(0).getCnpj8());
        assertEquals(ANO_MES, response.get(0).getAnoMes());
    }

    @Test
    void deveRetornarBuscarPorIdOk() {
        when(repo.findById(anyLong())).thenReturn(optionalTaxas);

        TaxaJurosResponse retorna = service.getTaxasById(ID);

        assertNotNull(retorna);
        assertEquals(TaxaJurosResponse.class, retorna.getClass());
        assertEquals(ID, retorna.getId());
    }

    @Test
    void deveRetornarBuscarPorIdNotFound() {
        assertThrows(TaxaJurosNotFoundException.class,
                () -> service.getTaxasById(ID));
    }

    @Test
    void deveCriarNovoItemOk() {
        validator = new CNPJValidator(true);

        when(repo.save(any())).thenReturn(taxasModel);

        validator.assertValid(validString);

        TaxaJurosResponse retorna = service.postNovaTaxa(taxasDto);

        assertNotNull(retorna);
        assertThat(mapper.modelToDto(taxasModel)).isEqualTo(taxasDto);
    }


    @Test
    void deveCriarNovoItemComDadosNulos() {
        TaxaJurosRequest model = taxasModel;
        TaxaJurosResponse dto = mapper.modelToDto(model);

        when(repo.save(model)).thenReturn(model);

        try {
            service.postNovaTaxa(dto);
        }catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
    }

    @Test
    void deveRetornarItemAtualizadoPorIdOk() {
        validator = new CNPJValidator(true);
        when(repo.findById(anyLong())).thenReturn(optionalTaxas);
        validator.assertValid(validString);
        when(repo.save(any())).thenReturn(taxasModel);

        TaxaJurosResponse retorna = service.updateTaxa(taxasDto);

        assertNotNull(retorna);
        assertThat(mapper.modelToDto(taxasModel)).isEqualTo(taxasDto);
        assertEquals(TaxaJurosResponse.class, retorna.getClass());
        assertEquals(ID, retorna.getId());
        assertEquals(MES, retorna.getMes());
        assertEquals(MODALIDADE, retorna.getModalidade());
        assertEquals(POSICAO,retorna.getPosicao());
        assertEquals(INSTITUICAO_FINANCEIRA, retorna.getInstituicaoFinanceira());
        assertEquals(TAXA_JUROS_AO_MES, retorna.getTaxaJurosAoMes());
        assertEquals(TAXA_JUROS_AO_ANO, retorna.getTaxaJurosAoAno());
        assertEquals(CNPJ_8, retorna.getCnpj8());
        assertEquals(ANO_MES, retorna.getAnoMes());
    }

    @Test
    void deveRetornarDeletarInstanciaOk() {
        when(repo.findById(anyLong())).thenReturn(optionalTaxas);
        doNothing().when(repo).deleteById(anyLong());
        service.deleteTaxa(ID);
        verify(repo, times(1)).deleteById(anyLong());
    }

    @Test
    void deveRetornarTodosOsItensPorAnoMesOk() {
        when(repo.buscarAnoMes(anyString()))
                .thenReturn((List.of(taxasModel)));
        when(repo.findAll()).thenReturn(List.of(taxasModel));

        List<TaxaJurosResponse> retorna = service.getTaxasPorAnoMes(taxasDto.getAnoMes());

        verify(repo, times(1)).buscarAnoMes(taxasDto.getAnoMes());
        assertNotNull(retorna);
        assertThat(mapper.modelToDto(taxasModel)).isEqualTo(taxasDto);
    }

    @Test
    void deveRetornarExceptionSeItemPorAnoMesForNulo() {
        List<TaxaJurosRequest> retornaNulo = new ArrayList<>();
        when(repo.findAll()).thenReturn(retornaNulo);

        List<TaxaJurosResponse> retorna = service.getTaxasPorAnoMes(taxasDto.getAnoMes());

        assertThat(retorna.size()).isEqualTo(0);
        assertThrows(TaxaJurosNotFoundException.class,
                () -> service.getTaxasPorAnoMes(String.valueOf(retorna.isEmpty())));
    }

    @Test
    void deveRetornarExceptionSeQueryPorAnoMesNaoForFormatado() {
        String valor = "02/2023";
        assertThrows(TaxaJurosNotFoundException.class,
               () -> service.getTaxasPorAnoMes(valor));
    }

    @Test
    void deveRetornarTodosOsItensPorPaginaETamanho() {
        Pageable pageable = PageRequest.of(0,1);
        List<TaxaJurosRequest> taxa = new ArrayList<>();
        Page<TaxaJurosRequest> resulta = new PageImpl<>(taxa, pageable, 1);
        when(repo.buscarPorPg(any(Pageable.class)))
                .thenReturn(resulta);

        Page<TaxaJurosResponse> retorna = service.getTaxasPorPagina(0,1);

        assertThat(retorna.getTotalElements()).isEqualTo(1);
        assertNotNull(retorna);
        assertThat(mapper.modelToDto(taxasModel)).isEqualTo(taxasDto);
    }

    private void gerarTaxas() {
        taxasModel = new TaxaJurosRequest(ID, MES, MODALIDADE, POSICAO,INSTITUICAO_FINANCEIRA,
                TAXA_JUROS_AO_MES, TAXA_JUROS_AO_ANO, CNPJ_8, ANO_MES);
        optionalTaxas = Optional.of(new TaxaJurosRequest(ID, MES, MODALIDADE, POSICAO,
                INSTITUICAO_FINANCEIRA, TAXA_JUROS_AO_MES, TAXA_JUROS_AO_ANO, CNPJ_8, ANO_MES));
        taxasDto = new TaxaJurosResponse(ID, MES, MODALIDADE, POSICAO,INSTITUICAO_FINANCEIRA,
                TAXA_JUROS_AO_MES, TAXA_JUROS_AO_ANO, CNPJ_8, ANO_MES);
        optionalTaxasDto = Optional.of(new TaxaJurosResponse(ID, MES, MODALIDADE, POSICAO,
                INSTITUICAO_FINANCEIRA, TAXA_JUROS_AO_MES, TAXA_JUROS_AO_ANO, CNPJ_8, ANO_MES));
        dto = new TaxaJurosDTO(List.of(taxasDto));
        mapper = TaxaJurosMapper.MAPPER;

    }

}
