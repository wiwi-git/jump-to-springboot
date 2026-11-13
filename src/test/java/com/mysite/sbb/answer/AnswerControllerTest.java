package com.mysite.sbb.answer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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

import com.mysite.sbb.user.UserService;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AnswerControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private UserService userService;

	// 질문·답변 등록은 로그인한 사용자만 할 수 있고(@PreAuthorize), 글쓴이로 저장할 회원이 DB에 있어야 한다
	@BeforeEach
	void setUp() {
		userService.create("user1", "user1@test.com", "1234");
	}

	@Test
	@DisplayName("답변 등록: 질문의 addAnswer로 저장되고 상세 페이지로 리다이렉트")
	void t1() throws Exception {
		long beforeCount = answerRepository.count();

		mvc
				.perform(post("/answer/create/2").with(csrf()).with(user("user1")).param("content", "새 답변입니다."))
				.andDo(print())
				.andExpect(handler().handlerType(AnswerController.class))
				.andExpect(handler().methodName("createAnswer"))
				.andExpect(status().is3xxRedirection())
				.andExpect(result -> assertThat(result.getResponse().getRedirectedUrl())
						.matches("/question/detail/2#answer_\\d+"));

		// count 쿼리 전에 flush되므로, cascade PERSIST로 답변이 insert되었는지 확인할 수 있다.
		assertThat(answerRepository.count()).isEqualTo(beforeCount + 1);

		List<Answer> answers = answerRepository.findAll();
		Answer lastAnswer = answers.get(answers.size() - 1);
		assertThat(lastAnswer.getContent()).isEqualTo("새 답변입니다.");
		assertThat(lastAnswer.getQuestion().getId()).isEqualTo(2);
		assertThat(lastAnswer.getAuthor().getUsername()).isEqualTo("user1");
	}

	@Test
	@DisplayName("답변 등록: 없는 질문이면 404")
	void t2() throws Exception {
		mvc
				.perform(post("/answer/create/999").with(csrf()).with(user("user1")).param("content", "새 답변입니다."))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("답변 등록: 공백만 입력하면 저장하지 않고 상세 화면에 오류 표시")
	void t3() throws Exception {
		long beforeCount = answerRepository.count();

		mvc
				.perform(post("/answer/create/2").with(csrf()).with(user("user1")).param("content", "   "))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("question_detail"))
				.andExpect(model().attributeHasFieldErrorCode("answerForm", "content", "NotBlank"));

		assertThat(answerRepository.count()).isEqualTo(beforeCount);
	}

	@Test
	@DisplayName("답변 등록: 비로그인이면 저장하지 않고 로그인 화면으로 보낸다")
	void t4() throws Exception {
		long beforeCount = answerRepository.count();

		mvc
				.perform(post("/answer/create/2").with(csrf()).param("content", "비로그인 답변"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user/login"));

		assertThat(answerRepository.count()).isEqualTo(beforeCount);
	}
}
