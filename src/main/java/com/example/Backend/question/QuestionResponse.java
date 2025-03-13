package com.example.Backend.question;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionResponse {
    private Long jobId;
    private String content;
    private String modelAnswer;

    public QuestionResponse(Long jobId, String content, String modelAnswer) {
        this.jobId = jobId;
        this.content = content;
        this.modelAnswer = modelAnswer;
    }
}
