package com.grepp.synapse4.app.model.restaurant;

import com.grepp.synapse4.app.model.user.dto.RankingDto;
import com.grepp.synapse4.app.model.user.repository.BookMarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RestaurantService {

    @Autowired
    private BookMarkRepository bookMarkRepository;

    public List<RankingDto> getRestaurantRanking() {
        return bookMarkRepository.findRestaurantRanking();
    }


}
