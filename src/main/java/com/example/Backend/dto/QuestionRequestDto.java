package com.example.Backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class QuestionRequestDto {

    @Schema(description = "인성 질문 개수", example = "3")
    private int personalityCount;

    @Schema(description = "직군 ID (0: 프론트엔드, 1: 백엔드)", example = "0")
    private Long jobId; //TODO 0이면 FE, 1이면 BE 현재는 FE만 가능

    @Schema(description = "기술 질문 개수", example = "3")
    private int techCount;
}
