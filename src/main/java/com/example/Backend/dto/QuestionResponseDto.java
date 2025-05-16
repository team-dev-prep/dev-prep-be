package com.example.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class QuestionResponseDto {
    private Long userId;
    private Long interviewId;
    private int totalCount;
    private List<QuestionDto> questions;
}
