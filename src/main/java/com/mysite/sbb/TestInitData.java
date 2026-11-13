package com.mysite.sbb;

import java.time.LocalDateTime;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.RequiredArgsConstructor;

@Profile("test")
@Configuration
@RequiredArgsConstructor
public class TestInitData {
	private final QuestionRepository questionRepository;
	private final AnswerRepository answerRepository;

	@Bean
	public ApplicationRunner testInitDataApplicationRunner() {
		return args -> work();
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
		questionRepository.save(q2); // 2번 질문

		Answer a = new Answer();
		a.setContent("네 자동으로 생성됩니다.");
		a.setQuestion(q2);
		a.setCreateDate(LocalDateTime.now());
		answerRepository.save(a); // 2번 질문의 1번 답변
	}
}
