package com.example.Backend.question;

import com.example.Backend.dto.QuestionDto;
import com.example.Backend.dto.QuestionRequestDto;
import com.example.Backend.dto.QuestionResponseDto;
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

    @Transactional
    public QuestionResponseDto getQuestionsForAuthenticatedUser(Long userId, QuestionRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        List<Question> personalityQuestions = questionRepository.findPersonalityQuestions(requestDto.getJobId(), requestDto.getPersonalityCount());

        List<Question> techQuestions = questionRepository.findTechQuestions(requestDto.getJobId(), requestDto.getTechCount());

        List<QuestionDto> questionDtos = personalityQuestions.stream()
                .map(q -> new QuestionDto(q.getId(), q.getTime(), q.getContent()))
                .collect(Collectors.toList());

        questionDtos.addAll(techQuestions.stream()
                .map(q -> new QuestionDto(q.getId(), q.getTime(), q.getContent()))
                .toList());

        return new QuestionResponseDto(user.getId(), requestDto.getPersonalityCount() + requestDto.getTechCount(), questionDtos);
    }

    public Question saveQuestion(Question question) { //TODO 2차 목표 진행 시 데이터베이스 만들 때 사용 예정
        return questionRepository.save(question);
    }
}
