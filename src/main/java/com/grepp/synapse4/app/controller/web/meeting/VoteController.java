package com.grepp.synapse4.app.controller.web.meeting;

import com.grepp.synapse4.app.controller.web.meeting.payload.vote.VoteRegistRequest;
import com.grepp.synapse4.app.model.meeting.VoteService;
import com.grepp.synapse4.app.model.meeting.dto.VoteDto;
import com.grepp.synapse4.app.model.meeting.entity.vote.Vote;
import com.grepp.synapse4.app.model.meeting.entity.vote.VoteMember;
import com.grepp.synapse4.app.model.user.BookMarkService;
import com.grepp.synapse4.app.model.user.CustomUserDetailsService;
import com.grepp.synapse4.app.model.user.entity.Bookmark;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class VoteController {

  private final CustomUserDetailsService customUserDetailsService;
  private final BookMarkService bookmarkService;
  private final VoteService voteService;

  @GetMapping("vote-regist")
  @PreAuthorize("isAuthenticated()")
  public String voteRegist(
      Model model,
      @RequestParam Long id
  ){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Long userId = customUserDetailsService.loadUserIdByAccount(authentication.getName());

    model.addAttribute("voteRegistRequest", new VoteRegistRequest());
    List<Bookmark> bookmarkList = bookmarkService.findByUserId(userId);
    model.addAttribute("bookmarkList", bookmarkList);
    model.addAttribute("id", id);

    return "meetings/vote/vote-regist";
  }

  @PostMapping("vote-regist")
  @PreAuthorize("isAuthenticated()")
  public String voteRegist(
      @Valid VoteRegistRequest form,
      @RequestParam Long id,
//      @RequestParam List<Long> selectedList,
      BindingResult bindingResult
  ){
    if(bindingResult.hasErrors()){
      return "redirect:/meetings/detail?id="+id;
    }
    VoteDto dto = form.toDto(id);
    Vote vote = voteService.registVote(dto);

    voteService.registVoteMember(vote, id);

    return "redirect:/meetings/detail?id="+id;
  }

  @GetMapping("vote")
  @PreAuthorize("isAuthenticated()")
  public String voteDetail(
      @RequestParam Long id,
      Model model
  ){
    Vote vote = voteService.findVoteByVoteId(id);
    model.addAttribute("vote", vote);

    return "meetings/vote/vote-detail";
  }

  @PostMapping("vote")
  @PreAuthorize("isAuthenticated()")
  public String voteDetail(
      @Valid Boolean isJoined,
      @RequestParam Long id
  ){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Long userId = customUserDetailsService.loadUserIdByAccount(authentication.getName());
    voteService.vote(id, userId, isJoined);

    Vote vote = voteService.findVoteByVoteId(id);

    return "redirect:/meetings/detail?id="+vote.getMeeting().getId();
  }

  @GetMapping("vote-result")
  @PreAuthorize("isAuthenticated()")
  public String voteResult(
      @RequestParam Long id,
      Model model
  ){
    Vote vote = voteService.findVoteByVoteId(id);
    model.addAttribute("vote", vote);

    List<VoteMember> joinedList = voteService.findJoinedListByVoteId(id, true);
    model.addAttribute("joinedList", joinedList);
    model.addAttribute("joinedCount", joinedList.size());
    List<VoteMember> notJoinedList = voteService.findJoinedListByVoteId(id, false);
    model.addAttribute("notJoinedList", notJoinedList);
    model.addAttribute("notJoinedCount", notJoinedList.size());
    log.info("joinedList: {}", joinedList);
    log.info("notJoinedList: {}", notJoinedList);

    return "meetings/vote/vote-result";
  }

  @GetMapping("/modal/alarm-vote.html")
  @PreAuthorize("isAuthenticated()")
  public String votePopup(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Long userId = customUserDetailsService.loadUserIdByAccount(authentication.getName());
    List<VoteMember> votedList = voteService.findVoteListByUserId(userId);
    model.addAttribute("votedList", votedList);

    return "meetings/modal/alarm-vote";
  }

  @PostMapping("/modal/alarm-vote.html")
  @PreAuthorize("isAuthenticated()")
  public String votePopup() {


    return "redirect:/meetings/modal/alarm-vote";
  }
}
