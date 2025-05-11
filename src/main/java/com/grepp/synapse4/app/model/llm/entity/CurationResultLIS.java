package com.grepp.synapse4.app.model.llm.entity;

import com.grepp.synapse4.app.model.restaurant.entity.Restaurant;
import com.grepp.synapse4.app.model.restaurant.entity.RestaurantLIS;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class CurationResultLIS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long curationResultId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="curation_id")
    private CurationLIS curation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private RestaurantLIS restaurant;

}
