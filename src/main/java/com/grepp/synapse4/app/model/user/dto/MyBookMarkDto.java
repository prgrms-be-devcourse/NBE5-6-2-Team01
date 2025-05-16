package com.grepp.synapse4.app.model.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString

public class MyBookMarkDto {

    private Long userId;
    private Long bookMarkId;
    private LocalDateTime createdAt;
    private Long restaurantId;
    private String restaurantName;
    private String restaurantAddress;


    public MyBookMarkDto(Long userId, Long bookMarkId, LocalDateTime createdAt, Long restaurantId, String restaurantName, String restaurantAddress) {
        this.userId = userId;
        this.bookMarkId = bookMarkId;
        this.createdAt = createdAt;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
    }
}
