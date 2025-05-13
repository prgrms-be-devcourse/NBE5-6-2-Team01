package com.grepp.synapse4.app.controller.web.admin;

import com.grepp.synapse4.app.model.user.UserService;
import com.grepp.synapse4.app.model.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("admin")
//@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    @GetMapping("users")
    public String users(Model model) {
        log.info("users");
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/users";
    }





}
