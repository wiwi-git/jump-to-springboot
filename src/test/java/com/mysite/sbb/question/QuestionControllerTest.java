package com.mysite.sbb.question;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.user.UserService;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class QuestionControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private UserService userService;

	// 질문·답변 등록은 로그인한 사용자만 할 수 있고(@PreAuthorize), 글쓴이로 저장할 회원이 DB에 있어야 한다
	@BeforeEach
	void setUp() {
		userService.create("user1", "user1@test.com", "1234");
	}

	@Test
	@DisplayName("질문 상세: 있는 질문이면 200과 제목·내용")
	void t1() throws Exception {
		ResultActions resultActions = mvc
				.perform(get("/question/detail/2"))
				.andDo(print());

		resultActions
				.andExpect(handler().handlerType(QuestionController.class))
				.andExpect(handler().methodName("detail"))
				.andExpect(status().isOk())
				.andExpect(view().name("question_detail"))
				.andExpect(content().string(containsString("스프링부트 모델 질문입니다.")));
	}

	@Test
	@DisplayName("질문 상세: 없는 질문이면 404와 DataNotFoundException")
	void t2() throws Exception {
		ResultActions resultActions = mvc
				.perform(get("/question/detail/999"))
				.andDo(print());

		resultActions
				.andExpect(status().isNotFound())
				.andExpect(result -> assertThat(result.getResolvedException())
						.isInstanceOf(DataNotFoundException.class)
						.hasMessage("question not found"));
	}

	@Test
	@DisplayName("질문 등록: 제목·내용이 있으면 저장하고 목록으로 리다이렉트")
	void t3() throws Exception {
		long beforeCount = questionRepository.count();

		mvc
				.perform(post("/question/create").with(csrf()).with(user("user1"))
						.param("subject", "새 질문")
						.param("content", "새 질문 내용"))
				.andDo(print())
				.andExpect(handler().methodName("questionCreate"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/question/list"));

		assertThat(questionRepository.count()).isEqualTo(beforeCount + 1);
		assertThat(questionRepository.findBySubject("새 질문").getContent()).isEqualTo("새 질문 내용");
	}

	@Test
	@DisplayName("질문 등록: 공백만 입력하면 저장하지 않고 폼과 오류 메시지를 다시 보여 줌")
	void t4() throws Exception {
		long beforeCount = questionRepository.count();

		mvc
				.perform(post("/question/create").with(csrf()).with(user("user1"))
						.param("subject", "   ")
						.param("content", "   "))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("question_form"))
				.andExpect(model().attributeHasFieldErrorCode("questionForm", "subject", "NotBlank"))
				.andExpect(model().attributeHasFieldErrorCode("questionForm", "content", "NotBlank"))
				.andExpect(content().string(containsString("제목은 필수항목입니다.")))
				.andExpect(content().string(containsString("내용은 필수항목입니다.")));

		assertThat(questionRepository.count()).isEqualTo(beforeCount);
	}

	@Test
	@DisplayName("질문 등록: 제목이 200자를 넘으면 오류")
	void t5() throws Exception {
		mvc
				.perform(post("/question/create").with(csrf()).with(user("user1"))
						.param("subject", "가".repeat(201))
						.param("content", "내용"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("question_form"))
				.andExpect(model().attributeHasFieldErrorCode("questionForm", "subject", "Size"));
	}

	@Test
	@DisplayName("질문 등록 폼: GET이면 빈 폼, 검증에 실패하면 입력한 값을 다시 채워 줌")
	void t6() throws Exception {
		mvc
				.perform(get("/question/create").with(user("user1")))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("question_form"))
				.andExpect(content().string(containsString("name=\"subject\" value=\"\"")));

		mvc
				.perform(post("/question/create").with(csrf()).with(user("user1"))
						.param("subject", "제목만 입력")
						.param("content", ""))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("question_form"))
				.andExpect(content().string(containsString("value=\"제목만 입력\"")));
	}

	@Test
	@DisplayName("CSRF: 질문 등록 폼에는 th:action이 넣어 준 _csrf 토큰이 있다")
	void t7() throws Exception {
		mvc
				.perform(get("/question/create").with(user("user1")))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("<input type=\"hidden\" name=\"_csrf\" value=\"")));
	}

	@Test
	@DisplayName("CSRF: 토큰 없이 POST하면 컨트롤러에 닿기 전에 403으로 막힌다")
	void t8() throws Exception {
		long beforeCount = questionRepository.count();

		mvc
				.perform(post("/question/create")
						.param("subject", "토큰 없는 질문")
						.param("content", "다른 사이트에서 보낸 요청"))
				.andDo(print())
				.andExpect(status().isForbidden())
				// 시큐리티 필터에서 막혀 DispatcherServlet이 컨트롤러 메서드를 찾지 않았다
				.andExpect(result -> assertThat(result.getHandler()).isNull());

		assertThat(questionRepository.count()).isEqualTo(beforeCount);
	}

	@Test
	@DisplayName("글쓴이: 로그인한 사용자가 질문을 등록하면 그 사용자가 글쓴이로 저장된다")
	void t9() throws Exception {
		mvc
				.perform(post("/question/create").with(csrf()).with(user("user1"))
						.param("subject", "글쓴이가 있는 질문")
						.param("content", "내용"))
				.andExpect(status().is3xxRedirection());

		Question question = questionRepository.findBySubject("글쓴이가 있는 질문");
		assertThat(question.getAuthor().getUsername()).isEqualTo("user1");
	}

	@Test
	@DisplayName("글쓴이: 비로그인이면 질문 등록 화면과 등록 요청 모두 로그인 화면으로 보낸다")
	void t10() throws Exception {
		long beforeCount = questionRepository.count();

		mvc
				.perform(get("/question/create"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user/login"));

		// CSRF 토큰은 있지만 로그인하지 않았다 → @PreAuthorize가 막는다
		mvc
				.perform(post("/question/create").with(csrf())
						.param("subject", "비로그인 질문")
						.param("content", "내용"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user/login"));

		assertThat(questionRepository.count()).isEqualTo(beforeCount);
	}
}
