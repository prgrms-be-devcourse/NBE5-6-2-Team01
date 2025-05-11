package com.grepp.synapse4.app.model.user.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Entity
@Table(name = "survey")
public class SurveyKCW {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long surveyId;
    private String companyLocation;
    private String purpose;
    private String companion;
    private String favoriteCategory;
    private String preferMood;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private UserKCW userKCW;



}
