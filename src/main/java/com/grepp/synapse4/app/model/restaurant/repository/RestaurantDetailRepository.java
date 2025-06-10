package com.grepp.synapse4.app.model.restaurant.repository;

import com.grepp.synapse4.app.model.restaurant.entity.Restaurant;
import com.grepp.synapse4.app.model.restaurant.entity.RestaurantDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantDetailRepository extends JpaRepository<RestaurantDetail, Long> {

    RestaurantDetail findByRestaurant(Restaurant restaurant);
}
