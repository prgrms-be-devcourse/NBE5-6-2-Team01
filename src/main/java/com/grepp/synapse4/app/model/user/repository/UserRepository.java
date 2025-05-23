package com.grepp.synapse4.app.model.user.repository;

import com.grepp.synapse4.app.model.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserAccount(String userAccount);
    Optional<User> findByNickname(String nickname);
    Optional<User> findByEmail(String email);

    boolean existsByUserAccount(String userAccount);
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);

}
