package com.grepp.synapse4.app.model.user;

import com.grepp.synapse4.app.model.user.entity.User;
import com.grepp.synapse4.app.model.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    public List<User> findAll() {
        return userRepository.findAll();
    }



}
