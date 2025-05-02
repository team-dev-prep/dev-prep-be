package com.example.Backend.question;

import com.example.Backend.dto.QuestionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PreQuestionResponseDto {
    private int total;
    private List<QuestionDto> questions;
}
