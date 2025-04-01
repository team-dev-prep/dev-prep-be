package com.example.Backend.answer;

import com.example.Backend.dto.AnswerRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AnswerApi {

    private final AnswerService answerService;

    @Operation(summary = "사용자 답변 저장", description = "질문의 ID에 맞는 답변을 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "답변 저장 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 또는 질문을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/answers")
    public ResponseEntity<String> submitAnswer(@RequestBody AnswerRequestDto requestDto) {
        answerService.saveAnswers(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Answer saved successfully");
    }
}
