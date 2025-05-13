package com.grepp.synapse4.app.controller.web.meeting.payload;

import com.grepp.synapse4.app.model.meeting.code.Purpose;
import com.grepp.synapse4.app.model.meeting.dto.MeetingDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MeetingRegistRequest {

  @NotBlank(message = "모임 이름은 필수입니다.")
  private String title;

  @NotBlank(message = "모임 설명은 필수입니다.")
  private String description;

  private Purpose purpose;

  private Boolean isDutch;

  public MeetingDto toDto(Long userId){
    MeetingDto meetingDto = new MeetingDto();

    meetingDto.setTitle(title);
    meetingDto.setDescription(description);
    meetingDto.setPurpose(purpose);
    meetingDto.setIsDutch(isDutch);
    meetingDto.setCreatorId(userId);

    return meetingDto;
  }

}
