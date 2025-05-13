package com.grepp.synapse4.app.model.meeting.entity;

import com.grepp.synapse4.app.model.meeting.code.Purpose;
import com.grepp.synapse4.app.model.meeting.entity.vote.Vote;
import com.grepp.synapse4.infra.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @ToString
public class Meeting extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "meeting_id")
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Purpose purpose;
    private Boolean isDutch;
    private Long creatorId;

    @OneToMany(mappedBy = "meeting")
    private List<MeetingMember> meetingMembers = new ArrayList<>();

    @OneToMany(mappedBy = "meeting")
    private List<Vote> votes = new ArrayList<>();
}