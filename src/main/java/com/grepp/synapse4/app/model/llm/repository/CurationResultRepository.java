package com.grepp.synapse4.app.model.llm.repository;

import com.grepp.synapse4.app.model.llm.dto.CurationResultDto;
import com.grepp.synapse4.app.model.llm.entity.CurationResult;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CurationResultRepository extends JpaRepository<CurationResult, Long> {

    @Query("""
      SELECT new com.grepp.synapse4.app.model.llm.dto.CurationResultDto(
        c.title,
        r.name,
        r.address
      )
      FROM CurationResult cr
      JOIN cr.curation c
      JOIN cr.restaurant r
      """)
    List<CurationResultDto> findResultsByCurationId();
}