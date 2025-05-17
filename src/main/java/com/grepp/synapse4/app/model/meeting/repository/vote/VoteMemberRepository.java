package com.grepp.synapse4.app.model.meeting.repository.vote;

import com.grepp.synapse4.app.model.meeting.entity.vote.VoteMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteMemberRepository extends JpaRepository<VoteMember, Long> {

}
