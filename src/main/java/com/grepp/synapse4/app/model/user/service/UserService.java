package com.grepp.synapse4.app.model.user.service;

import static com.grepp.synapse4.app.model.auth.code.Role.ROLE_USER;
import com.grepp.synapse4.app.model.user.dto.request.UserSignUpRequest;
import com.grepp.synapse4.app.model.user.entity.User;
import com.grepp.synapse4.app.model.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(UserSignUpRequest request){
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = request.toEntity(encodedPassword, ROLE_USER);
        userRepository.save(user);
    }

    public void validateDuplicateUser(UserSignUpRequest request, BindingResult bindingResult) {
        if (userRepository.existsByUserAccount(request.getUserAccount())) {
            bindingResult.rejectValue("userAccount", "duplicate", "이미 등록된 아이디입니다.");
        }

        if (userRepository.existsByNickname(request.getNickname())) {
            bindingResult.rejectValue("nickname", "duplicate", "이미 등록된 닉네임입니다.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            bindingResult.rejectValue("email", "duplicate", "이미 등록된 이메일입니다.");
        }
    }

}
