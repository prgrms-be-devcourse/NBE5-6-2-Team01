package com.grepp.synapse4.app.model.llm.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class CurationLIS{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long curationId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String companyLocation;

    @Column(nullable = false)
    private String purpose;

    @Column(nullable = false)
    private String companion;

    @Column(nullable = false)
    private String favoriteCategory;

    @Column(nullable = false)
    private String preferredMood;

    @OneToMany(mappedBy = "curation", cascade = CascadeType.ALL)
    private List<CurationResultLIS> results = new ArrayList<>();
}
