package com.example.Backend.answer;

import com.example.Backend.dto.AnswerRequestDto;
import com.example.Backend.question.Question;
import com.example.Backend.question.QuestionRepository;
import com.example.Backend.user.User;
import com.example.Backend.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public void saveAnswers(AnswerRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Question question = questionRepository.findById(requestDto.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        Answer answer = new Answer(user, question, requestDto.getUserAnswer());
        answerRepository.save(answer);
    }

    public Optional<Answer> getAnswerByQuestionId(Long questionId) {
        return answerRepository.findByQuestionId(questionId);
    }
}
