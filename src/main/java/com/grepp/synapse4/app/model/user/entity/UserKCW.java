package com.grepp.synapse4.app.model.user.entity;

import com.grepp.synapse4.infra.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;


@Getter @Setter @ToString
@Entity
@Table(name = "users")
public class UserKCW extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String userAccount;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isSurvey = false;


//    BaseEntity 로 빼놨습니다

    // 활성화 (계정삭제시 비활성화)
//    private boolean activated;

    // 생성날짜
//    private LocalDateTime createAt;

    // 삭제날짜
//    private LocalDateTime deleteAt;




}

