package com.mysite.sbb;

import java.time.LocalDateTime;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import com.mysite.sbb.question.QuestionService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class TestInitData {
	private final QuestionRepository questionRepository;
	private final QuestionService questionService;
	private final Environment environment;

	@Bean
	public ApplicationRunner testInitDataApplicationRunner() {
		return args -> {
			work();
			if (environment.matchesProfiles("dev")) workPagingData();
		};
	}

	private void work() {
		if (questionRepository.count() > 0) return;

		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		questionRepository.save(q1); // 1번 질문

		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		q2.addAnswer("네 자동으로 생성됩니다.", null); // 2번 질문의 1번 답변
		questionRepository.save(q2); // 2번 질문 (cascade PERSIST로 답변도 함께 저장)
	}

	// 교재 3-02: 페이징을 확인할 테스트 데이터 300개 (개발 DB에 한 번만)
	private void workPagingData() {
		if (questionRepository.findBySubject("테스트 데이터입니다:[001]") != null) return;

		for (int i = 1; i <= 300; i++) {
			String subject = String.format("테스트 데이터입니다:[%03d]", i);
			String content = "내용무";
			this.questionService.create(subject, content, null);
		}
	}
}
