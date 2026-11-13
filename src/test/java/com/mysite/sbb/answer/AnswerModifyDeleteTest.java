package com.mysite.sbb.answer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AnswerModifyDeleteTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private AnswerService answerService;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private UserService userService;

	private Integer answerId;

	// 2번 질문에 user1이 쓴 답변 하나, 다른 사용자 user2
	@BeforeEach
	void setUp() {
		SiteUser user1 = userService.create("user1", "user1@test.com", "1234");
		userService.create("user2", "user2@test.com", "1234");
		Question question = questionService.getQuestion(2);
		answerService.create(question, "user1의 답변", user1);
		List<Answer> answers = answerRepository.findAll(); // flush되어 답변이 insert된다
		answerId = answers.get(answers.size() - 1).getId();
	}

	@Test
	@DisplayName("답변 수정 화면: 글쓴이에게는 기존 내용이 채워진 폼, 다른 사용자는 400")
	void t1() throws Exception {
		mvc
				.perform(get("/answer/modify/" + answerId).with(user("user1")))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("answer_form"))
				.andExpect(content().string(containsString("user1의 답변</textarea>")));

		mvc
				.perform(get("/answer/modify/" + answerId).with(user("user2")))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("답변 수정: 글쓴이만 수정되고 수정일시가 기록된다")
	void t2() throws Exception {
		mvc
				.perform(post("/answer/modify/" + answerId).with(csrf()).with(user("user2"))
						.param("content", "남이 바꾼 답변"))
				.andExpect(status().isBadRequest());

		assertThat(answerRepository.findById(answerId).get().getContent()).isEqualTo("user1의 답변");

		mvc
				.perform(post("/answer/modify/" + answerId).with(csrf()).with(user("user1"))
						.param("content", "수정한 답변"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/question/detail/2"));

		Answer answer = answerRepository.findById(answerId).get();
		assertThat(answer.getContent()).isEqualTo("수정한 답변");
		assertThat(answer.getModifyDate()).isNotNull();
	}

	@Test
	@DisplayName("답변 삭제: _method=DELETE로 보낸 요청에서 글쓴이만 삭제된다")
	void t3() throws Exception {
		mvc
				.perform(post("/answer/delete/" + answerId).with(csrf()).with(user("user2"))
						.param("_method", "DELETE"))
				.andExpect(status().isBadRequest());

		assertThat(answerRepository.findById(answerId)).isPresent();
		long beforeCount = answerRepository.count();

		mvc
				.perform(post("/answer/delete/" + answerId).with(csrf()).with(user("user1"))
						.param("_method", "DELETE"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/question/detail/2"));

		// count 쿼리 전에 flush되므로 delete SQL이 실제로 실행되었는지 확인할 수 있다
		assertThat(answerRepository.count()).isEqualTo(beforeCount - 1);
		assertThat(answerRepository.findById(answerId)).isEmpty();
	}

	@Test
	@DisplayName("답변 삭제: GET 링크로는 삭제할 수 없다(405)")
	void t4() throws Exception {
		mvc
				.perform(get("/answer/delete/" + answerId).with(user("user1")))
				.andExpect(status().isMethodNotAllowed());

		assertThat(answerRepository.findById(answerId)).isPresent();
	}
}
