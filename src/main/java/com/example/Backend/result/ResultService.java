package com.example.Backend.result;

import com.example.Backend.answer.Answer;
import com.example.Backend.answer.AnswerRepository;
import com.example.Backend.dto.ResultResponseDto;
import com.example.Backend.question.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final AnswerRepository answerRepository;

    public List<ResultResponseDto> getUserResults(Long userId) {
        List<Answer> answers = answerRepository.findByUserId(userId);

        return answers.stream()
                .map(answer -> {
                    Question question = answer.getQuestion();
                    return new ResultResponseDto(
                            question.getId(),
                            question.getContent(),
                            question.getModelAnswer(),
                            answer.getUserAnswer()
                    );
                })
                .collect(Collectors.toList());
    }
}
