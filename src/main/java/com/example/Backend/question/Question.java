package com.example.Backend.question;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "question")
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long jobId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String modelAnswer;

    public Question(Long jobId, String content, String modelAnswer) {
        this.jobId = jobId;
        this.content = content;
        this.modelAnswer = modelAnswer;
    }
}
