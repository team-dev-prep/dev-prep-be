package com.example.Backend.result;

import com.example.Backend.dto.ResultResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ResultApi {

    private final ResultService resultService;

    @Operation(summary = "결과 조회", description = "사용자의 질문 및 답변을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결과 조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 답변 없음")
    })
    @GetMapping("/result")
    public ResponseEntity<ResultResponseDto> getResult(
            @Parameter(description = "사용자 ID", example = "1") @RequestParam Long userId) {
        ResultResponseDto results = resultService.getUserResults(userId);
        return ResponseEntity.ok(results);
    }
}
