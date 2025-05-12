package com.example.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PreQuestionDto {
    private Long id;
    private Long time;
    private String content;
    private String modelAnswer;
}
