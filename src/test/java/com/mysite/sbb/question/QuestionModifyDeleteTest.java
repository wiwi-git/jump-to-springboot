package com.mysite.sbb.question;

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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class QuestionModifyDeleteTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private UserService userService;

	private Integer questionId;

	// user1이 쓴 질문 하나, 다른 사용자 user2
	@BeforeEach
	void setUp() {
		SiteUser user1 = userService.create("user1", "user1@test.com", "1234");
		userService.create("user2", "user2@test.com", "1234");
		questionService.create("user1의 질문", "user1의 내용", user1);
		questionId = questionRepository.findBySubject("user1의 질문").getId();
	}

	@Test
	@DisplayName("질문 수정 화면: 글쓴이에게는 기존 값이 채워진 폼, 다른 사용자는 400")
	void t1() throws Exception {
		mvc
				.perform(get("/question/modify/" + questionId).with(user("user1")))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("question_form"))
				.andExpect(content().string(containsString("value=\"user1의 질문\"")))
				.andExpect(content().string(containsString("user1의 내용</textarea>")));

		mvc
				.perform(get("/question/modify/" + questionId).with(user("user2")))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("질문 수정: 글쓴이만 수정되고 수정일시가 기록된다")
	void t2() throws Exception {
		mvc
				.perform(post("/question/modify/" + questionId).with(csrf()).with(user("user2"))
						.param("subject", "남이 바꾼 제목")
						.param("content", "남이 바꾼 내용"))
				.andDo(print())
				.andExpect(status().isBadRequest());

		assertThat(questionRepository.findById(questionId).get().getSubject()).isEqualTo("user1의 질문");

		mvc
				.perform(post("/question/modify/" + questionId).with(csrf()).with(user("user1"))
						.param("subject", "수정한 제목")
						.param("content", "수정한 내용"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/question/detail/" + questionId));

		Question question = questionRepository.findById(questionId).get();
		assertThat(question.getSubject()).isEqualTo("수정한 제목");
		assertThat(question.getContent()).isEqualTo("수정한 내용");
		assertThat(question.getModifyDate()).isNotNull();
	}

	@Test
	@DisplayName("질문 삭제: _method=DELETE로 보낸 요청에서 글쓴이만 삭제된다")
	void t3() throws Exception {
		mvc
				.perform(post("/question/delete/" + questionId).with(csrf()).with(user("user2"))
						.param("_method", "DELETE"))
				.andDo(print())
				.andExpect(status().isBadRequest());

		assertThat(questionRepository.findById(questionId)).isPresent();

		mvc
				.perform(post("/question/delete/" + questionId).with(csrf()).with(user("user1"))
						.param("_method", "DELETE"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"));

		assertThat(questionRepository.findById(questionId)).isEmpty();
	}

	@Test
	@DisplayName("질문 삭제: GET 링크로는 삭제할 수 없다(405)")
	void t4() throws Exception {
		mvc
				.perform(get("/question/delete/" + questionId).with(user("user1")))
				.andDo(print())
				.andExpect(status().isMethodNotAllowed());

		assertThat(questionRepository.findById(questionId)).isPresent();
	}

	@Test
	@DisplayName("질문 상세: 글쓴이에게는 CSRF 토큰과 _method=DELETE가 든 삭제 폼이 보인다")
	void t5() throws Exception {
		mvc
				.perform(get("/question/detail/" + questionId).with(user("user1")))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(
						"<form action=\"/question/delete/" + questionId + "\" method=\"post\"")))
				.andExpect(content().string(containsString("<input type=\"hidden\" name=\"_method\" value=\"DELETE\">")));

		mvc
				.perform(get("/question/detail/" + questionId).with(user("user2")))
				.andExpect(status().isOk())
				.andExpect(content().string(org.hamcrest.Matchers.not(containsString("/question/delete/"))));
	}
}
