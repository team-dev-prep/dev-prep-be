package com.example.Backend.dto;

import com.example.Backend.result.ResultDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ResultResponseDto {
    private Long userId;
    private int totalCount;
    private List<ResultDto> results;
}