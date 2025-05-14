package com.grepp.synapse4.app.model.meeting;

import com.grepp.synapse4.app.model.meeting.code.State;
import com.grepp.synapse4.app.model.meeting.dto.MeetingDto;
import com.grepp.synapse4.app.model.meeting.entity.Meeting;
import com.grepp.synapse4.app.model.meeting.entity.MeetingMember;
import com.grepp.synapse4.app.model.meeting.repository.MeetingMemberRepository;
import com.grepp.synapse4.app.model.meeting.repository.MeetingRepository;
import com.grepp.synapse4.app.model.user.entity.User;
import com.grepp.synapse4.app.model.user.repository.UserRepository;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingService {

  private final MeetingRepository meetingRepository;
  private final ModelMapper mapper;
  private final UserRepository userRepository;
  private final MeetingMemberRepository meetingMemberRepository;

  @Transactional
  public void registMeeting(MeetingDto dto){
    Meeting meeting = mapper.map(dto, Meeting.class);
    meetingRepository.save(meeting);

    User user = userRepository.findById(dto.getCreatorId())
                      .orElseThrow(() -> new RuntimeException("유저를 찾지 못했습니다."));
    MeetingMember meetingMember = new MeetingMember();
    meetingMember.setState(State.ACCEPT);
    meetingMember.setMeeting(meeting);
    meetingMember.setUser(user);

    meetingMemberRepository.save(meetingMember);
  }

  @Transactional
  public List<Meeting> findMeetingsById(Long userId){
    List<MeetingMember> meetingMemberList =  meetingMemberRepository.findAllByUserIdAndDeletedAtIsNull(userId);
    log.info("meetingMemberList: {}", meetingMemberList);

    return meetingMemberList.stream()
        .map(MeetingMember::getMeeting)
        .sorted(Comparator.comparing(Meeting::getCreatedAt).reversed())
        .collect(Collectors.toList());
  }
}
