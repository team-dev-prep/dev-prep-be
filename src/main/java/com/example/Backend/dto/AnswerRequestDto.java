package com.example.Backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnswerRequestDto {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "질문 ID", example = "3")
    private Long questionId;

    @Schema(description = "사용자가 작성한 답변", example = "3번 질문에 대한 답안입니다.")
    private String userAnswer;
}
