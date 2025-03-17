package com.example.Backend.question;

import com.example.Backend.answer.Answer;
import com.example.Backend.answer.AnswerService;
import com.example.Backend.response.InterviewResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerService answerService;

    public Question saveQuestion(Question question) { //TODO 1차 목표 진행 시 데이터베이스 만들 때 사용 예정
        return questionRepository.save(question);
    }

    public Optional<Question> getQuestion(Long id) {
        return questionRepository.findById(id);
    }

    public Optional<InterviewResultResponse> getInterviewResult(Long questionId) {
        Optional<Question> questionOpt = questionRepository.findById(questionId);
        if (questionOpt.isEmpty()) return Optional.empty();

        Question question = questionOpt.get();
        Optional<Answer> answerOpt = answerService.getAnswerByQuestionId(questionId);
        return Optional.of(new InterviewResultResponse(question, answerOpt.orElse(null)));
    }
}
