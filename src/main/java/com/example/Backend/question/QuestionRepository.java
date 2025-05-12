package com.example.Backend.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(
            value = "SELECT * FROM question WHERE job_id = :jobId AND personality = 1 ORDER BY RAND() LIMIT :count",
            nativeQuery = true
    )
    List<Question> findPersonalityQuestions(@Param("jobId") Long jobId, @Param("count") int count);

    @Query(
            value = "SELECT * FROM question WHERE job_id = :jobId AND personality = 0 ORDER BY RAND() LIMIT :count",
            nativeQuery = true
    )
    List<Question> findTechQuestions(@Param("jobId") Long jobId, @Param("count") int count);
}
