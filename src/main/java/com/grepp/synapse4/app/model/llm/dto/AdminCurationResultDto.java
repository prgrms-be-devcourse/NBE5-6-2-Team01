package com.grepp.synapse4.app.model.llm.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString

public class AdminCurationResultDto {

    private Long id;
    private String title;
    private String name;
    private String address;
    private boolean active;

    public AdminCurationResultDto(Long id, String title, String name, String address, Boolean active) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.address = address;
        this.active = active;
    }

}