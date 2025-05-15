package com.grepp.synapse4.app.model.llm.dto;

import com.grepp.synapse4.app.model.llm.entity.Curation;
import com.grepp.synapse4.app.model.restaurant.entity.Restaurant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString

public class CurationResultDto {

    private String title;
    private String name;
    private String address;

    public CurationResultDto(String title, String name, String address) {
        this.title = title;
        this.name = name;
        this.address = address;
    }
}