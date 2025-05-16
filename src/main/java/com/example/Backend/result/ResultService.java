package com.example.Backend.result;

import com.example.Backend.answer.Answer;
import com.example.Backend.answer.AnswerRepository;
import com.example.Backend.dto.ResultDto;
import com.example.Backend.dto.ResultResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final AnswerRepository answerRepository;

    public ResultResponseDto getUserResults(Long interviewId, Long userId) {
        List<Answer> answers = answerRepository.findByInterviewId(interviewId);

        List<ResultDto> result = answers.stream()
                .map(answer -> new ResultDto(
                        answer.getQuestion().getId(),
                        answer.getQuestion().getContent(),
                        answer.getQuestion().getModelAnswer(),
                        answer.getUserAnswer()
                ))
                .toList();

        return new ResultResponseDto(
                userId,
                result.size(),
                result
        );
    }
}
