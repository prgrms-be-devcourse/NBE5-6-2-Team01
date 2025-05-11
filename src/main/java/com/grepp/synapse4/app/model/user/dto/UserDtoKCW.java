package com.grepp.synapse4.app.model.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString

public class UserDtoKCW {

    private Long userId;
    private String userAccount;
    private String password;
    private String nickname;
    private String email;
    private boolean isSurvey;
}
