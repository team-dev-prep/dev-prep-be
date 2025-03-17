package com.example.Backend.answer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    public Optional<Answer> getAnswerByQuestionId(Long questionId) {
        return answerRepository.findByQuestionId(questionId);
    }

    public void saveAnswer(Answer answer) {
        answerRepository.save(answer);
    }
}
