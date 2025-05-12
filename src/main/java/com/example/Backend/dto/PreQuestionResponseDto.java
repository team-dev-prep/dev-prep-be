package com.example.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PreQuestionResponseDto {
    private int total;
    private List<PreQuestionDto> questions;
}
