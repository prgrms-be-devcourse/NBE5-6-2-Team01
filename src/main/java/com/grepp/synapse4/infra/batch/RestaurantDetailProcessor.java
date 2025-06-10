package com.grepp.synapse4.infra.batch;

import com.grepp.synapse4.app.model.restaurant.dto.create.CsvRestaurantDetailDto;
import com.grepp.synapse4.app.model.restaurant.entity.Restaurant;
import com.grepp.synapse4.app.model.restaurant.entity.RestaurantDetail;
import com.grepp.synapse4.app.model.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
@StepScope
@RequiredArgsConstructor
@Slf4j
public class RestaurantDetailProcessor implements ItemProcessor<CsvRestaurantDetailDto, RestaurantDetail> {

    private final RestaurantRepository restaurantRepository;

    private int count=0;

    @Override
    @Transactional(readOnly = true)
    public RestaurantDetail process(CsvRestaurantDetailDto dto) throws Exception {
        Restaurant restaurant = restaurantRepository.findByPublicId(dto.getPublicId());

        count++;
        if (count % 100 == 0) {
            log.info("🍜 Processed {} items", count);
        }

        if (restaurant == null || Objects.equals(restaurant.getPublicId(), "12317")) {
            log.debug("Restaurant not found for publicId: {}", dto.getPublicId());
            return null;
        }



        RestaurantDetail detail = RestaurantDetail.builder()
                .restaurant(restaurant)
                .dayOff(dto.getDayOff())
                .rowBusinessTime(dto.getRowBusinessTime())
                .parking(dto.getParking())
                .wifi(dto.getWifi())
                .delivery(dto.getDelivery())
                .homePageURL(dto.getHomePageURL())
                .build();

        detail.setId(restaurant.getId());

        return detail;
    }
}