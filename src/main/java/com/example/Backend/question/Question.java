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

    @Column(name = "personality", nullable = false)
    private Long personality;

    @Column(name = "jobId", nullable = false)
    private Long jobId;

    @Column(name = "time", nullable = false)
    private Long time;

    @Column(name = "question", nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "modelAnswer", nullable = false, columnDefinition = "LONGTEXT")
    private String modelAnswer;

    public Question(Long personality, Long jobId, Long time, String content, String modelAnswer) {
        this.personality = personality;
        this.jobId = jobId;
        this.time = time;
        this.content = content;
        this.modelAnswer = modelAnswer;
    }
}
