package com.example.Backend.question;

import com.example.Backend.dto.*;
import com.example.Backend.interview.Interview;
import com.example.Backend.interview.InterviewRepository;
import com.example.Backend.user.User;
import com.example.Backend.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final InterviewRepository interviewRepository;

    @Transactional
    public PreQuestionResponseDto getPreQuestions(Long jobId) {
        List<Question> personality = questionRepository.findPersonalityQuestions(0L, 1);
        List<Question> tech = questionRepository.findTechQuestions(jobId, 1);

        List<PreQuestionDto> questionDtos = personality.stream()
                .map(q -> new PreQuestionDto(q.getId(), q.getTime(), q.getContent(), q.getModelAnswer()))
                .collect(Collectors.toList());

        questionDtos.addAll(tech.stream()
                .map(q -> new PreQuestionDto(q.getId(), q.getTime(), q.getContent(), q.getModelAnswer()))
                .toList());

        return new PreQuestionResponseDto(questionDtos.size(), questionDtos);
    }

    @Transactional
    public QuestionResponseDto getQuestionsForAuthenticatedUser(Long userId, QuestionRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        Interview interview = interviewRepository.save(new Interview(user));

        List<Question> personalityQuestions = questionRepository.findPersonalityQuestions(0L, requestDto.getPersonalityCount());
        List<Question> techQuestions = questionRepository.findTechQuestions(requestDto.getJobId(), requestDto.getTechCount());

        List<QuestionDto> questionDtos = personalityQuestions.stream()
                .map(q -> new QuestionDto(q.getId(), q.getTime(), q.getContent()))
                .collect(Collectors.toList());

        questionDtos.addAll(techQuestions.stream()
                .map(q -> new QuestionDto(q.getId(), q.getTime(), q.getContent()))
                .toList());

        return new QuestionResponseDto(user.getId(), interview.getId(), questionDtos.size(), questionDtos);
    }

    public Question saveQuestion(Question question) { //TODO 2차 목표 진행 시 데이터베이스 만들 때 사용 예정
        return questionRepository.save(question);
    }
}
