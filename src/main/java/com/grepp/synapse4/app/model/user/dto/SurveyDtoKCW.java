package com.grepp.synapse4.app.model.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString

public class SurveyDtoKCW {

    private Long surveyId;
    private String companyLocation;
    private String purpose;
    private String companion;
    private String favoriteCategory;
    private String preferMood;
    private Long userId;


}
