package com.example.Backend.api;

import com.example.Backend.response.InterviewResultResponse;
import com.example.Backend.question.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interview")
@RequiredArgsConstructor
public class InterviewApi {

    private final QuestionService questionService;

    @Operation(summary = "모범답안 및 사용자의 답안 조회", description = "질문의 ID 값으로 해당 질문의 모범 답안과 사용자의 답안을 함께 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모범답안 및 사용자 답안 조회 성공"),
            @ApiResponse(responseCode = "404", description = "질문을 찾을 수 없습니다")
    })
    @GetMapping("/result/{id}")
    public ResponseEntity<InterviewResultResponse> getInterviewResult(
            @Parameter(description = "조회할 질문의 ID") @PathVariable("id") Long id) {

        return questionService.getInterviewResult(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
