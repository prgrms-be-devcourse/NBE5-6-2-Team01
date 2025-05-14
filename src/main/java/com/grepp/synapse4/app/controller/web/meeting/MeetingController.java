package com.grepp.synapse4.app.controller.web.meeting;

import com.grepp.synapse4.app.controller.web.meeting.payload.MeetingRegistRequest;
import com.grepp.synapse4.app.model.meeting.MeetingService;
import com.grepp.synapse4.app.model.meeting.code.Purpose;
import com.grepp.synapse4.app.model.meeting.dto.MeetingDto;
import com.grepp.synapse4.app.model.meeting.entity.Meeting;
import com.grepp.synapse4.app.model.user.entity.User;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
    List<Meeting> meetingList = meetingService.findMeetingsById(1L);
    model.addAttribute("meetingList", meetingList);

    return "meetings/meetings";
  }

  @GetMapping("regist")
  public String regist(Model model) {
    model.addAttribute("meetingRegistRequest", new MeetingRegistRequest());
    model.addAttribute("purpose", Purpose.values());
    return "meetings/meetingRegist";
  }

  @PostMapping("regist")
  public String regist(
      @Valid MeetingRegistRequest form,
      BindingResult bindingResult,
      Model model
  ){
    if(bindingResult.hasErrors()){
      log.info("error: {}", bindingResult.hasErrors());
      model.addAttribute("purpose", Purpose.values());
      return "meetings";
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
  public String detail(Model model){
    return "meetings/meetingDetail";
  }

  @GetMapping("/alarm/invite.html")
  public String invitePopup() {
    return "meetings/alarm/invite";  // templates/meetings/alarm/invite.html
  }

  @GetMapping("/alarm/vote.html")
  public String votePopup() {
    return "meetings/alarm/vote";  // templates/meetings/alarm/vote.html
  }

}
