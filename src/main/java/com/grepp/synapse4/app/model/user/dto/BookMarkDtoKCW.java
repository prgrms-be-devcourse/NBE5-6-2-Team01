package com.grepp.synapse4.app.model.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter @ToString

public class BookMarkDtoKCW {

    private Long userId;
    private Long bookMarkId;
//    private Long restaurantId;
    private LocalDateTime createdAt;


}
