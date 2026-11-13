package com.mysite.sbb;

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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.UserService;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class VoteTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private AnswerService answerService;

	@Autowired
	private UserService userService;

	@BeforeEach
	void setUp() {
		userService.create("user1", "user1@test.com", "1234");
		userService.create("user2", "user2@test.com", "1234");
	}

	@Test
	@DisplayName("질문 추천: 같은 사람이 여러 번 추천해도 한 번만 센다")
	void t1() throws Exception {
		mvc
				.perform(post("/question/vote/2").with(csrf()).with(user("user1")))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/question/detail/2"));
		mvc.perform(post("/question/vote/2").with(csrf()).with(user("user1")));
		mvc.perform(post("/question/vote/2").with(csrf()).with(user("user2")));

		assertThat(questionService.getQuestion(2).getVoter())
				.extracting("username")
				.containsExactlyInAnyOrder("user1", "user2");
	}

	@Test
	@DisplayName("답변 추천: 추천인이 답변의 voter에 들어가고 질문 상세로 돌아간다")
	void t2() throws Exception {
		mvc
				.perform(post("/answer/vote/1").with(csrf()).with(user("user1")))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/question/detail/2"));
		mvc.perform(post("/answer/vote/1").with(csrf()).with(user("user1")));

		Answer answer = answerService.getAnswer(1);
		assertThat(answer.getVoter()).extracting("username").containsExactly("user1");
		assertThat(questionService.getQuestion(2).getVoter()).isEmpty();
	}

	@Test
	@DisplayName("추천: GET 링크로는 추천되지 않는다(405)")
	void t3() throws Exception {
		mvc
				.perform(get("/question/vote/2").with(user("user1")))
				.andExpect(status().isMethodNotAllowed());
		mvc
				.perform(get("/answer/vote/1").with(user("user1")))
				.andExpect(status().isMethodNotAllowed());

		assertThat(questionService.getQuestion(2).getVoter()).isEmpty();
		assertThat(answerService.getAnswer(1).getVoter()).isEmpty();
	}

	@Test
	@DisplayName("추천: 토큰이 없으면 403, 비로그인이면 로그인 화면으로")
	void t4() throws Exception {
		mvc
				.perform(post("/question/vote/2").with(user("user1")))
				.andExpect(status().isForbidden());
		mvc
				.perform(post("/question/vote/2").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user/login"));

		assertThat(questionService.getQuestion(2).getVoter()).isEmpty();
	}

	@Test
	@DisplayName("질문 상세: 추천 버튼은 CSRF 토큰이 든 POST 폼이고 추천 수를 보여 준다")
	void t5() throws Exception {
		mvc.perform(post("/question/vote/2").with(csrf()).with(user("user1")));

		mvc
				.perform(get("/question/detail/2"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("<form action=\"/question/vote/2\" method=\"post\"")))
				.andExpect(content().string(containsString("<form action=\"/answer/vote/1\" method=\"post\"")))
				.andExpect(content().string(containsString("<span class=\"badge rounded-pill bg-success\">1</span>")));
	}
}
