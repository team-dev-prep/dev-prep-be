package com.example.Backend.answer;

import com.example.Backend.question.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interview")
@RequiredArgsConstructor
public class AnswerApi {

    private final AnswerService answerService;
    private final QuestionService questionService;

    @Operation(summary = "사용자 답변 저장", description = "질문의 ID에 맞는 답변을 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "답변 저장 성공"),
            @ApiResponse(responseCode = "404", description = "질문을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/{question_id}")
    public ResponseEntity<Void> saveAnswer(
            @Parameter(description = "답변을 저장할 질문의 ID") @PathVariable("question_id") Long questionId,
            @Parameter(description = "사용자의 답변") @RequestBody AnswerRequest request) {

        return questionService.getQuestion(questionId)
                .map(question -> {
                    answerService.saveAnswer(new Answer(question, request.getContents()));
                    return ResponseEntity.status(HttpStatus.CREATED).<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
