package com.mysite.sbb.user;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
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

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserLoginTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private UserService userService;

	@BeforeEach
	void setUp() {
		userService.create("user1", "user1@test.com", "1234");
	}

	@Test
	@DisplayName("로그인: 가입한 아이디·비밀번호면 인증되고 메인으로")
	void t1() throws Exception {
		mvc
				.perform(formLogin("/user/login").user("user1").password("1234"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"))
				.andExpect(authenticated().withUsername("user1").withRoles("USER"));
	}

	@Test
	@DisplayName("로그인: 비밀번호가 틀리면 /user/login?error")
	void t2() throws Exception {
		mvc
				.perform(formLogin("/user/login").user("user1").password("9999"))
				.andDo(print())
				.andExpect(redirectedUrl("/user/login?error"))
				.andExpect(unauthenticated());
	}

	@Test
	@DisplayName("내비게이션: 로그인하면 로그아웃 버튼은 CSRF 토큰이 든 POST 폼")
	void t3() throws Exception {
		mvc
				.perform(get("/question/list"))
				.andExpect(content().string(containsString("href=\"/user/login\"")))
				.andExpect(content().string(not(containsString("로그아웃"))));

		mvc
				.perform(get("/question/list").with(user("user1")))
				.andDo(print())
				.andExpect(content().string(not(containsString("href=\"/user/login\""))))
				.andExpect(content().string(containsString(
						"<form action=\"/user/logout\" method=\"post\"><input type=\"hidden\" name=\"_csrf\"")));
	}

	@Test
	@DisplayName("로그아웃: CSRF 토큰과 함께 POST하면 로그아웃되고 메인으로")
	void t4() throws Exception {
		mvc
				.perform(post("/user/logout").with(csrf()).with(user("user1")))
				.andDo(print())
				.andExpect(redirectedUrl("/"))
				.andExpect(unauthenticated());
	}

	@Test
	@DisplayName("로그아웃: GET 요청(예: 다른 사이트의 이미지 태그)으로는 로그아웃되지 않음")
	void t5() throws Exception {
		mvc
				.perform(get("/user/logout").with(user("user1")))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(authenticated().withUsername("user1"));
	}
}
