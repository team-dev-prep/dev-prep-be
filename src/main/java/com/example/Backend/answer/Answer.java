package com.example.Backend.answer;

import com.example.Backend.interview.Interview;
import com.example.Backend.question.Question;
import com.example.Backend.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "Answer")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "userAnswer", nullable = false, columnDefinition = "LONGTEXT")
    private String userAnswer;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id", nullable = false)
    private Interview interview;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new java.util.Date();
    }

    public Answer(User user, Question question, String userAnswer, Interview interview) {
        this.user = user;
        this.question = question;
        this.userAnswer = userAnswer;
        this.interview = interview;
        this.createdAt = new java.util.Date();
    }
}
