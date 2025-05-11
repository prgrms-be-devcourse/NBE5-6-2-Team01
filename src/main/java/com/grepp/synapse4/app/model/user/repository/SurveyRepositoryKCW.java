package com.grepp.synapse4.app.model.user.repository;

import com.grepp.synapse4.app.model.user.entity.SurveyKCW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepositoryKCW extends JpaRepository<SurveyKCW, Long> {



}
