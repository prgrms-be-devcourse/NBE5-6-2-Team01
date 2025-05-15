package com.grepp.synapse4.app.controller.web.admin;

import com.grepp.synapse4.app.model.llm.CurationResultService;
import com.grepp.synapse4.app.model.llm.CurationService;
import com.grepp.synapse4.app.model.llm.code.*;
import com.grepp.synapse4.app.model.llm.dto.CurationDto;
import com.grepp.synapse4.app.model.llm.dto.CurationResultDto;
import com.grepp.synapse4.app.model.llm.entity.CurationResult;
import com.grepp.synapse4.app.model.meeting.MeetingService;
import com.grepp.synapse4.app.model.meeting.entity.Meeting;
import com.grepp.synapse4.app.model.user.UserService;
import com.grepp.synapse4.app.model.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("admin")
//@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final MeetingService meetingService;
    private final CurationService curationService;
    private final CurationResultService curationResultService;

    @GetMapping("users")
    public String users(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @GetMapping("meetings")
    public String meetings(Model model) {
        List<Meeting> meetings = meetingService.findAll();
        model.addAttribute("meetings", meetings);
        return "admin/meetings";
    }

    @GetMapping("curation/register")
    public String curationRegister(Model model) {

        model.addAttribute("form", new CurationDto());
        model.addAttribute("companylocation", CompanyLocation.values());
        model.addAttribute("purpose", Purpose.values());
        model.addAttribute("compainons", Companion.values());
        model.addAttribute("favoritecategory", FavoriteCategory.values());
        model.addAttribute("preferredmood", PreferredMood.values());

        return "admin/curationRegister";
    }

    @PostMapping("curation/register")
    public String curationRegister(@ModelAttribute("form") CurationDto curationDto) {
        curationService.setCuration(curationDto);
        return "redirect:/admin/curation/list";
    }

    @GetMapping("curation/list")
    public String curationList(Model model) {
        List<CurationResultDto> curationResults = curationResultService.getResultsByCurationId();
        model.addAttribute("curationResults", curationResults);
        return "admin/curationList";
    }

    @GetMapping("signup")
    public String signup(Model model) {
        return "admin/signup";
    }

    @GetMapping("signin")
    public String signin(Model model) {
        return "admin/signin";
    }

}
