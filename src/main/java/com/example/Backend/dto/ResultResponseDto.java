package com.example.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResultResponseDto {
    private Long id;
    private String content;
    private String modelAnswer;
    private String userAnswer;
}