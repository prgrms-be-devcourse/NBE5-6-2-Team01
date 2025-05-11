package com.grepp.synapse4.app.model.user.repository;

import com.grepp.synapse4.app.model.user.entity.UserKCW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryKCW extends JpaRepository<UserKCW, Long> {
}
