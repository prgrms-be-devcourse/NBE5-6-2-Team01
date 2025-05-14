package com.grepp.synapse4.app.controller.web.user;

import com.grepp.synapse4.app.model.user.dto.CustomUserDetails;
import com.grepp.synapse4.app.model.user.dto.request.EditInfoRequest;
import com.grepp.synapse4.app.model.user.entity.User;
import com.grepp.synapse4.app.model.user.repository.UserRepository;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @GetMapping
    public String myPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        User user = userDetails.getUser();
        model.addAttribute("user", user);
        return "user/mypage";
    }

    @GetMapping("/edit-info")
    public String editInfoForm(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userDetails.getUser();
        EditInfoRequest dto = new EditInfoRequest();
        dto.setUserAccount(user.getUserAccount());
        dto.setNickname(user.getNickname());
        dto.setEmail(user.getEmail());
        model.addAttribute("editRequest", dto);
        return "user/edit-info";
    }

    @PostMapping("/edit-info")
    public String updateInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @ModelAttribute("editRequest") EditInfoRequest request,
        BindingResult bindingResult) {
        User user = userDetails.getUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            bindingResult.rejectValue("currentPassword", "invalid", "기존 비밀번호가 일치하지 않습니다.");
        }

        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
                bindingResult.rejectValue("newPassword", "sameAsOld", "기존 비밀번호와 동일합니다.");
            }
        }

        Optional<User> userByNickname = userRepository.findByNickname(request.getNickname());
        if (userByNickname.isPresent() && !userByNickname.get().getId().equals(user.getId())) {
            bindingResult.rejectValue("nickname", "duplicate", "이미 사용 중인 닉네임입니다.");
        }

        Optional<User> userByEmail = userRepository.findByEmail(request.getEmail());
        if (userByEmail.isPresent() && !userByEmail.get().getId().equals(user.getId())) {
            bindingResult.rejectValue("email", "duplicate", "이미 등록된 이메일입니다.");
        }

        if (bindingResult.hasErrors()) {
            return "user/edit-info";
        }

        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        userRepository.save(user);
        return "redirect:/mypage";
    }

}
