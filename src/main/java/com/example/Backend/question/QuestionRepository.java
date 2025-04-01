package com.example.Backend.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    //TODO 2차목표 진행 시 값을 랜덤하게 가져올 수 있도록 수정

    @Query("SELECT q FROM Question q WHERE q.jobId = :jobId AND q.personality = 1 ORDER BY RAND() LIMIT :count")
    List<Question> findPersonalityQuestions(@Param("jobId") Long jobId, @Param("count") int count);

    @Query("SELECT q FROM Question q WHERE q.jobId = :jobId AND q.personality = 0 ORDER BY RAND() LIMIT :count")
    List<Question> findTechQuestions(@Param("jobId") Long jobId, @Param("count") int count);
}
