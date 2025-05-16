package com.grepp.synapse4.app.model.meeting;

import com.grepp.synapse4.app.model.meeting.dto.VoteDto;
import com.grepp.synapse4.app.model.meeting.entity.Meeting;
import com.grepp.synapse4.app.model.meeting.entity.vote.Vote;
import com.grepp.synapse4.app.model.meeting.repository.MeetingRepository;
import com.grepp.synapse4.app.model.meeting.repository.VoteRepository;
import com.grepp.synapse4.app.model.restaurant.entity.Restaurant;
import com.grepp.synapse4.app.model.restaurant.repository.RestaurantRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class VoteService {

  private final VoteRepository voteRepository;
  private final MeetingRepository meetingRepository;
  private final RestaurantRepository restaurantRepository;
  private final ModelMapper mapper;

  public void registVote(VoteDto dto) {
    Vote vote = new Vote();
    Meeting meeting = meetingRepository.findById(dto.getMeetingId())
        .orElseThrow(() -> new RuntimeException("모임을 찾지 못했습니다"));
    Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
        .orElseThrow(() -> new RuntimeException("식당을 찾지 못했습니다."));

    vote.setTitle(dto.getTitle());
    vote.setDescription(dto.getDescription());
    vote.setMeetingDate(dto.getMeetingDate());
    vote.setEndedAt(dto.getEndedAt());
    vote.setIsDutch(dto.getIsDutch());
    vote.setMeeting(meeting);
    vote.setRestaurant(restaurant);

    voteRepository.save(vote);
  }

  public List<Vote> findVoteListById(Long meetingId) {
    return voteRepository.findAllByMeetingId(meetingId);
  }
}
