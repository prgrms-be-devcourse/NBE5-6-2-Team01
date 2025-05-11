package com.grepp.synapse4.app.model.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @ToString
@Entity
@Table(name = "bookmark")
public class BookMarkKCW {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkId;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private UserKCW userKCW;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "restaurantId", nullable = false)
//    private 레스토랑 ;



}
