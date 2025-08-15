package com.cdac.eventnexus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackRequestDto {
    private Long userId;
    private Long eventId;
    private String comment;
    private Integer rating;
}