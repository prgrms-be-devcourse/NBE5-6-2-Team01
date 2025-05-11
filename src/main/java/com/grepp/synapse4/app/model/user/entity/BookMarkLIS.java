package com.grepp.synapse4.app.model.user.entity;

import com.grepp.synapse4.app.model.restaurant.entity.RestaurantLIS;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
public class BookMarkLIS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmark_id;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserLIS user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private RestaurantLIS restaurant;


}
