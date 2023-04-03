package com.eliza.feignClient.repositories;

import com.eliza.feignClient.model.TaxaJurosRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxaJurosRepository extends JpaRepository<TaxaJurosRequest, Long> {
    @Query("SELECT p FROM TaxaJurosRequest p WHERE p.anoMes LIKE %?1%")
    List<TaxaJurosRequest> buscarAnoMes(String valor);

    @Query("SELECT p FROM TaxaJurosRequest p")
    Page<TaxaJurosRequest> buscarPorPg(@Param("buscar") Pageable pageable);

}
