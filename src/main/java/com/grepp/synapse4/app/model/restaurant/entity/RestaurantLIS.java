package com.grepp.synapse4.app.model.restaurant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
public class RestaurantLIS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;

    @Column(nullable = false)
    private String restaurantName;

    @Column(nullable = false)
    private String restaurantLocation;

    @Column(nullable = false)
    private String restaurantAddress;

    @Column(nullable = false)
    private String restaurantLatitude;

    @Column(nullable = false)
    private String restaurantLongitude;

    private String kakaoId;
    private String publicId;
    private LocalDateTime createdAt;

}
