package com.mysite.sbb;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class QuestionRepositoryTest {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Test
	@DisplayName("findAll")
	void t1() {
		List<Question> questions = questionRepository.findAll();
		assertThat(questions).hasSize(2);

		Question question = questions.get(0);
		assertThat(question.getSubject()).isEqualTo("sbb가 무엇인가요?");
	}

	@Test
	@DisplayName("findById")
	void t2() {
		Question question = questionRepository.findById(1).get();
		assertThat(question.getSubject()).isEqualTo("sbb가 무엇인가요?");
	}

	@Test
	@DisplayName("findBySubject")
	void t3() {
		Question question = questionRepository.findBySubject("sbb가 무엇인가요?");
		assertThat(question.getId()).isEqualTo(1);
	}

	@Test
	@DisplayName("findBySubjectAndContent")
	void t4() {
		Question question = questionRepository.findBySubjectAndContent(
				"sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
		assertThat(question.getId()).isEqualTo(1);
	}

	@Test
	@DisplayName("findBySubjectLike")
	void t5() {
		List<Question> questions = questionRepository.findBySubjectLike("sbb%");
		assertThat(questions.get(0).getSubject()).isEqualTo("sbb가 무엇인가요?");
	}

	@Test
	@DisplayName("수정")
	void t6() {
		Question question = questionRepository.findById(1).get();
		question.setSubject("수정된 제목");
		questionRepository.save(question);

		assertThat(questionRepository.findBySubject("수정된 제목")).isNotNull();
	}

	@Test
	@DisplayName("삭제")
	void t7() {
		assertThat(questionRepository.count()).isEqualTo(2);

		Question question = questionRepository.findById(1).get();
		questionRepository.delete(question);

		assertThat(questionRepository.count()).isEqualTo(1);
	}

	@Test
	@DisplayName("답변 생성")
	void t8() {
		Question question = questionRepository.findById(2).get();

		Answer answer = new Answer();
		answer.setContent("네 자동으로 생성됩니다.");
		answer.setQuestion(question);
		answer.setCreateDate(LocalDateTime.now());
		answerRepository.save(answer);

		assertThat(answer.getId()).isNotNull();
	}

	@Test
	@DisplayName("답변 조회")
	void t9() {
		Answer answer = answerRepository.findById(1).get();
		assertThat(answer.getQuestion().getId()).isEqualTo(2);
	}

	@Test
	@DisplayName("질문에 달린 답변 목록 조회")
	void t10() {
		Question question = questionRepository.findById(2).get();

		List<Answer> answerList = question.getAnswerList();
		assertThat(answerList).hasSize(1);
		assertThat(answerList.get(0).getContent()).isEqualTo("네 자동으로 생성됩니다.");
	}

	@Test
	@DisplayName("답변 생성 by OneToMany")
	void t11() {
		Question question = questionRepository.findById(2).get();
		int beforeCount = question.getAnswerList().size();

		Answer newAnswer = question.addAnswer("네 자동으로 생성됩니다.");
		assertThat(question.getAnswerList()).hasSize(beforeCount + 1);

		// 아직 DB에 insert 전이라 id가 없다. flush하면 cascade PERSIST로 답변이 저장된다.
		assertThat(newAnswer.getId()).isNull();
		questionRepository.flush();
		assertThat(newAnswer.getId()).isNotNull();
	}
}
