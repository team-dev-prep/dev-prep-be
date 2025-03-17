package com.example.Backend.response;

import com.example.Backend.answer.Answer;
import com.example.Backend.question.Question;
import lombok.Getter;

@Getter
public class InterviewResultResponse {
    private final Long questionId;
    private final String questionContent;
    private final String modelAnswer;
    private final String userAnswer;

    public InterviewResultResponse(Question question, Answer answer) {
        this.questionId = question.getId();
        this.questionContent = question.getContent();
        this.modelAnswer = question.getModelAnswer() != null ? question.getModelAnswer() : null;
        this.userAnswer = answer != null ? answer.getContent() : null;
    }
}
