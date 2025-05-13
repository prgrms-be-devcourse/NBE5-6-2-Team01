package com.grepp.synapse4.app.controller.web.meeting;

import com.grepp.synapse4.app.model.meeting.MeetingService;
import com.grepp.synapse4.app.model.meeting.entity.Meeting;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    List<Meeting> meetingList = new ArrayList<>();
    model.addAttribute("meetingList", meetingList);

    return "meetings/meetings";
  }

  @GetMapping("create")
  public String create(Model model){
    return "meetings/meetingCreate";
  }

  @PostMapping("create")
  public String create(){

    return "redirect:/meetings";
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
