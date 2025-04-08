package com.example.Backend.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResultDto {
    private Long id;
    private String content;
    private String modelAnswer;
    private String userAnswer;
}
