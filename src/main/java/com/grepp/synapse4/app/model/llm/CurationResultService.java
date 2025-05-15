package com.grepp.synapse4.app.model.llm;

import com.grepp.synapse4.app.model.llm.dto.CurationResultDto;
import com.grepp.synapse4.app.model.llm.repository.CurationResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CurationResultService {

    private final CurationResultRepository repository;


    public List<CurationResultDto> getResultsByCurationId() {
        return repository.findResultsByCurationId();
    }

}
