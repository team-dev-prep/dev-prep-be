package com.example.Backend.question;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuestionApi {

    private final QuestionService questionService;

//    @PostMapping
//    public ResponseEntity<Question> saveQuestion(@RequestBody Question question) {
//        Question savedQuestion = questionService.saveQuestion(question);
//        return new ResponseEntity<>(savedQuestion, HttpStatus.CREATED);
//    }
    @Operation(summary = "질문 조회", description = "주어진 ID로 질문 내용을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "질문 조회 성공"),
        @ApiResponse(responseCode = "404", description = "질문을 찾을 수 없습니다")
    })
    @GetMapping("/question/{id}")
    public ResponseEntity<Question> getQuestion(
            @Parameter(description = "조회할 질문의 ID") @PathVariable("id") Long id) {
        return questionService.getQuestion(id)
                .map(question -> new ResponseEntity<>(new Question(null, question.getContent(), null), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @Operation(summary = "모델답안 조회", description = "주어진 ID로 질문의 모델 답안을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모델답안 조회 성공"),
            @ApiResponse(responseCode = "404", description = "질문을 찾을 수 없습니다")
    })
    @GetMapping("/question/result/{id}")
    public ResponseEntity<Question> getAnswer(
            @Parameter(description = "조회할 질문의 ID") @PathVariable("id") Long id) {
        return questionService.getQuestion(id)
                .map(question -> new ResponseEntity<>(new Question(null, null, question.getModelAnswer()), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
