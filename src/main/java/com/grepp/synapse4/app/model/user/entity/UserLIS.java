package com.grepp.synapse4.app.model.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
public class UserLIS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userAccount;
    private String password;
    private String nickname;
    private String email;
    private Boolean isSurvey;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    //추가 Role
    @Enumerated(EnumType.STRING)
    private RoleLIS role;

}
