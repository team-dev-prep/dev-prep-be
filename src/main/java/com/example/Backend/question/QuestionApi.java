package com.example.Backend.question;


import com.example.Backend.config.JwtUtil;
import com.example.Backend.dto.PreQuestionRequestDto;
import com.example.Backend.dto.PreQuestionResponseDto;
import com.example.Backend.dto.QuestionRequestDto;
import com.example.Backend.dto.QuestionResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QuestionApi {

    private final QuestionService questionService;
    private final JwtUtil jwtUtil;

    @PostMapping("/prequestion")
    @Operation(
            summary = "비로그인용 질문 제공 API",
            description = "로그인하지 않은 사용자에게 인성 질문 1개와 기술 질문 1개를 랜덤으로 제공합니다."
    )
    public PreQuestionResponseDto getPreQuestions(@RequestBody PreQuestionRequestDto requestDto) {
        return questionService.getPreQuestions(requestDto.getJobId());
    }

    @Operation(summary = "질문 가져오는 API", description = "인성과 기술 질문을 보내주면 수량에 맞게 질문을 보내줍니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "응답 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/question")
    public ResponseEntity<?> requestQuestions(@RequestBody QuestionRequestDto requestDto,
                                              @CookieValue(name = "access_token", required = false) String token) {
        if (token == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access token missing");

        try {
            String userId = jwtUtil.validateAndExtractSubject(token);
            QuestionResponseDto responseDto = questionService.getQuestionsForAuthenticatedUser(Long.valueOf(userId), requestDto);
            return ResponseEntity.ok(responseDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @Operation(summary = "질문 저장", description = "데이터 베이스 구축을 위한 질문 저장 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "질문 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/register") //TODO 데이터베이스 만들 때 사용할 질문 등록을 위한 API 입니다.
    public ResponseEntity<Question> saveQuestion(@RequestBody Question question) {
        Question savedQuestion = questionService.saveQuestion(question);
        return new ResponseEntity<>(savedQuestion, HttpStatus.CREATED);
    }
}
