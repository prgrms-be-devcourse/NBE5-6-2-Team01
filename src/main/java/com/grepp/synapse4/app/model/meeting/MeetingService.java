package com.grepp.synapse4.app.model.meeting;

import com.grepp.synapse4.app.model.meeting.code.State;
import com.grepp.synapse4.app.model.meeting.dto.MeetingDto;
import com.grepp.synapse4.app.model.meeting.dto.MeetingMemberDto;
import com.grepp.synapse4.app.model.meeting.entity.Meeting;
import com.grepp.synapse4.app.model.meeting.entity.MeetingMember;
import com.grepp.synapse4.app.model.meeting.repository.MeetingMemberRepository;
import com.grepp.synapse4.app.model.meeting.repository.MeetingRepository;
import com.grepp.synapse4.app.model.user.entity.User;
import com.grepp.synapse4.app.model.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MeetingService {

  private final MeetingRepository meetingRepository;
  private final ModelMapper mapper;
  private final UserRepository userRepository;
  private final MeetingMemberRepository meetingMemberRepository;

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

  public List<Meeting> findMeetingsByUserId(Long userId){
    List<MeetingMember> meetingMemberList =  meetingMemberRepository.findAllByUserIdAndStateAndDeletedAtIsNull(userId, State.ACCEPT);
    log.info("meetingMemberList: {}", meetingMemberList);

    return meetingMemberList.stream()
        .map(MeetingMember::getMeeting)
        .sorted(Comparator.comparing(Meeting::getCreatedAt).reversed())
        .collect(Collectors.toList());
  }

  public Meeting findMeetingsById(Long id) {
    return meetingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("모임을 찾지 못했습니다."));
  }

  public List<User> findMemberListByMeetingId(Long meetingId, State state) {
    List<MeetingMember> memberList = meetingMemberRepository.findAllByMeetingIdAndStateAndDeletedAtIsNull(meetingId, state);

    return memberList.stream()
        .map(MeetingMember::getUser)
        .collect(Collectors.toList());
  }

  public Boolean findMemberByMeetingIdAndUserId(Long meetingId, Long userId) {
    // TODO: JPA에서 MeetingMember 엔티티에 외래키로 사용중인 meeting_id, user_id를
    //        join을 하며 where를 meeting_id == user_id로 하는 문제 발생
    //    return meetingMemberRepository.existsByMeeting_IdAndUser_Id(meetingId, userId);
    List<MeetingMember> acceptedList = meetingMemberRepository.findAllByMeetingIdAndStateAndDeletedAtIsNull(meetingId, State.ACCEPT);
    List<MeetingMember> waitedList = meetingMemberRepository.findAllByMeetingIdAndStateAndDeletedAtIsNull(meetingId, State.WAIT);

    for(MeetingMember member:acceptedList){
      if(Objects.equals(member.getUser().getId(), userId)) return false;
    }
    for(MeetingMember member:waitedList){
      if(Objects.equals(member.getUser().getId(), userId)) return false;
    }

    return true;
  }

  public List<MeetingMember> findInviteByUserId(Long userId) {
    return meetingMemberRepository.findAllByUserIdAndStateAndDeletedAtIsNull(userId, State.WAIT);
  }

  public void setInviteModel(Model model, Long meetingId, String errorMessage) {
    List<User> invitedList = this.findMemberListByMeetingId(meetingId, State.WAIT);
    model.addAttribute("invitedList", invitedList);
    model.addAttribute("meetingId", meetingId);
    if (errorMessage != null) {
      model.addAttribute("error", errorMessage);
    }
  }

  public void inviteUser(MeetingMemberDto dto) {
    MeetingMember meetingMember = mapper.map(dto, MeetingMember.class);
    meetingMemberRepository.save(meetingMember);
  }

  public boolean updateInvitedState(Long meetingId, Long userId, String state) {
    MeetingMember meetingMember = meetingMemberRepository.findByMeetingIdAndUserId(meetingId, userId)
        .orElseThrow(() -> new EntityNotFoundException("데이터를 찾지 못했습니다"));

    if (state.equals("REJECT")) {
      meetingMemberRepository.delete(meetingMember);
      return false;
    } else if (state.equals("ACCEPT")) {
      meetingMember.setState(State.ACCEPT);
      meetingMemberRepository.save(meetingMember);
      return true;
    }

    return false;
  }
}
