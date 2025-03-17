package com.example.Backend.question;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class QuestionApi {

    private final QuestionService questionService;

    @PostMapping("/question") //TODO 질문 등록을 위한 API 입니다. 1차 목표 진행 시 데이터베이스 만들 때 사용 예정
    public ResponseEntity<Question> saveQuestion(@RequestBody Question question) {
        Question savedQuestion = questionService.saveQuestion(question);
        return new ResponseEntity<>(savedQuestion, HttpStatus.CREATED);
    }

    @Operation(summary = "질문 조회", description = "질문의 ID 값으로 해당 질문 내용을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "질문 조회 성공"),
        @ApiResponse(responseCode = "404", description = "질문을 찾을 수 없습니다")
    })
    @GetMapping("/interview/{id}")
    public ResponseEntity<Question> getQuestion(
            @Parameter(description = "조회할 질문의 ID, MVP 개발 중이므로 현재는 1번만 가능합니다.") @PathVariable("id") Long id) {
        return questionService.getQuestion(id)
                .map(question -> new ResponseEntity<>(new Question(null, question.getContent(), null), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
