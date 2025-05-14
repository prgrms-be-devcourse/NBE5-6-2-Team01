package com.grepp.synapse4.app.controller.web.meeting;

import com.grepp.synapse4.app.controller.web.meeting.payload.MeetingRegistRequest;
import com.grepp.synapse4.app.model.meeting.MeetingService;
import com.grepp.synapse4.app.model.meeting.code.Purpose;
import com.grepp.synapse4.app.model.meeting.dto.MeetingDto;
import com.grepp.synapse4.app.model.meeting.entity.Meeting;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("meetings")
@RequiredArgsConstructor
public class MeetingController {

  private final MeetingService meetingService;

  @GetMapping
  public String meeting(Model model){
    List<Meeting> meetingList = meetingService.findMeetingsByUserId(1L);
    model.addAttribute("meetingList", meetingList);

    return "meetings/meetings";
  }

  @GetMapping("regist")
  public String regist(Model model) {
    model.addAttribute("meetingRegistRequest", new MeetingRegistRequest());
    model.addAttribute("purpose", Purpose.values());
    return "meetings/meeting-regist";
  }

  @PostMapping("regist")
  public String regist(
      @Valid MeetingRegistRequest form,
      BindingResult bindingResult,
      Model model
  ){
    if(bindingResult.hasErrors()){
      model.addAttribute("purpose", Purpose.values());
      return "meetings/meeting-regist";
    }

//    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//    User user = (User) auth.getPrincipal();
//    Long userId = user.getId();

    MeetingDto dto = form.toDto(1L);
    log.info("dto: {}",dto);
    meetingService.registMeeting(dto);

    return "redirect:/meetings";
  }

  @GetMapping("{id}")
  public String detail(
      @PathVariable Long id,
      Model model
  ){
    Meeting meeting = meetingService.findMeetingsById(id);
    model.addAttribute(meeting);

    return "meetings/meeting-detail";
  }

  @GetMapping("/modal/invite.html")
  public String invitePopup() {
    return "meetings/modal/invite";
  }

  @GetMapping("/modal/vote.html")
  public String votePopup() {
    return "meetings/modal/vote";
  }

  @GetMapping("/modal/meeting-member-list.html")
  public String mmetingMemberListPopup() {
    return "meetings/modal/meeting-member-list";
  }

}
