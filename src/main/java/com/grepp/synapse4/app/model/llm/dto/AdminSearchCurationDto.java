package com.grepp.synapse4.app.model.llm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminSearchCurationDto {
    private Long curationResultId;
    private String title;
    private Long restaurantId;
    private String restaurantName;
    private String category;
    private String address;
    private String branch;
    private boolean active;

    public AdminSearchCurationDto(Long curationResultId, String title, Long restaurantId, String restaurantName, String category, String address, String branch, boolean active) {
        this.curationResultId = curationResultId;
        this.title = title;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.category = category;
        this.address = address;
        this.branch = branch;
        this.active = true;
    }
}
