package com.grepp.synapse4.app.model.meeting.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString

public class MeetingDto {

    private Long meetingId;
    private String title;
    private String creatorId;
    private String description;



}
