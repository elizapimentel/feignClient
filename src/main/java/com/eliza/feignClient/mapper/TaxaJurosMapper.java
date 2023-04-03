package com.eliza.feignClient.mapper;

import com.eliza.feignClient.model.TaxaJurosRequest;
import com.eliza.feignClient.model.dto.TaxaJurosResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TaxaJurosMapper {
    //instancia de implementacao da iterface
    TaxaJurosMapper MAPPER = Mappers.getMapper(TaxaJurosMapper.class);
    TaxaJurosResponse modelToDto(TaxaJurosRequest model);
    TaxaJurosRequest dtoToModel(TaxaJurosResponse dto);
}
