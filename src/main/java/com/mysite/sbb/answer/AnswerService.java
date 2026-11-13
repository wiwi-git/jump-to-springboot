package com.mysite.sbb.answer;

import com.mysite.sbb.question.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AnswerService {

	private final AnswerRepository answerRepository;

	// 답변은 질문 쪽에서 만든다(Question.addAnswer). 트랜잭션이 끝날 때 cascade PERSIST로 함께 저장된다.
	@Transactional
	public void create(Question question, String content) {
		question.addAnswer(content);
	}
}
