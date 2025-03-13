package com.example.Backend.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

//    public Question saveQuestion(Question question) {
//        return questionRepository.save(question);
//    }

    public Optional<Question> getQuestion(Long id) {
        return questionRepository.findById(id);
    }
}
