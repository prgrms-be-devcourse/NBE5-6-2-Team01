package com.grepp.synapse4.app.controller.web.meeting;

import com.grepp.synapse4.app.controller.web.meeting.payload.MeetingRegistRequest;
import com.grepp.synapse4.app.model.meeting.MeetingService;
import com.grepp.synapse4.app.model.meeting.code.Purpose;
import com.grepp.synapse4.app.model.meeting.code.State;
import com.grepp.synapse4.app.model.meeting.dto.MeetingDto;
import com.grepp.synapse4.app.model.meeting.entity.Meeting;
import com.grepp.synapse4.app.model.user.CustomUserDetailsService;
import com.grepp.synapse4.app.model.user.UserService;
import com.grepp.synapse4.app.model.user.dto.CustomUserDetails;
import com.grepp.synapse4.app.model.user.entity.User;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("meetings")
@RequiredArgsConstructor
public class MeetingController {

  private final MeetingService meetingService;
  private final CustomUserDetailsService customUserDetailsService;

  @GetMapping
  public String meeting(Model model){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Long userId = customUserDetailsService.loadUserIdByAccount(authentication.getName());

    List<Meeting> meetingList = meetingService.findMeetingsByUserId(userId);
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

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Long userId = customUserDetailsService.loadUserIdByAccount(authentication.getName());

    MeetingDto dto = form.toDto(userId);
    meetingService.registMeeting(dto);

    return "redirect:/meetings";
  }

  @GetMapping("detail")
  public String detail(
      @RequestParam Long id,
      Model model
  ){
    Meeting meeting = meetingService.findMeetingsById(id);
    model.addAttribute("meeting", meeting);

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
  public String meetingMemberListPopup(
      @RequestParam Long id,
      Model model
  ) {
    Meeting meeting = meetingService.findMeetingsById(id);
    model.addAttribute("meeting", meeting);

    List<User> userList = meetingService.findMemberListById(id);
    model.addAttribute("userList", userList);

    return "meetings/modal/meeting-member-list";
  }

}
