package com.mysite.sbb.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	@DisplayName("회원가입: 정상 입력이면 저장하고 메인으로 리다이렉트, 비밀번호는 암호화")
	void t1() throws Exception {
		mvc
				.perform(post("/user/signup").with(csrf())
						.param("username", "user1")
						.param("password1", "1234")
						.param("password2", "1234")
						.param("email", "user1@test.com"))
				.andDo(print())
				.andExpect(handler().handlerType(UserController.class))
				.andExpect(handler().methodName("signup"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"));

		SiteUser user = userRepository.findAll().getFirst();
		assertThat(user.getUsername()).isEqualTo("user1");
		assertThat(user.getEmail()).isEqualTo("user1@test.com");
		assertThat(user.getPassword()).isNotEqualTo("1234").startsWith("$2a$");
		assertThat(passwordEncoder.matches("1234", user.getPassword())).isTrue();
	}

	@Test
	@DisplayName("회원가입: 공백만·빈 값을 입력하면 저장하지 않고 필드마다 NotBlank 오류")
	void t2() throws Exception {
		mvc
				.perform(post("/user/signup").with(csrf())
						.param("username", "   ")
						.param("password1", "   ")
						.param("password2", "   ")
						.param("email", ""))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("signup_form"))
				.andExpect(model().attributeHasFieldErrorCode("userCreateForm", "username", "NotBlank"))
				.andExpect(model().attributeHasFieldErrorCode("userCreateForm", "password1", "NotBlank"))
				.andExpect(model().attributeHasFieldErrorCode("userCreateForm", "password2", "NotBlank"))
				// 이메일은 빈 값으로 보낸다. "   "이면 @Email 오류도 함께 생겨 어느 코드가 먼저 나올지 정해져 있지 않다
				.andExpect(model().attributeHasFieldErrorCode("userCreateForm", "email", "NotBlank"));

		assertThat(userRepository.count()).isZero();
	}

	@Test
	@DisplayName("회원가입: 두 비밀번호가 다르면 password2에 오류")
	void t3() throws Exception {
		mvc
				.perform(post("/user/signup").with(csrf())
						.param("username", "user1")
						.param("password1", "1234")
						.param("password2", "5678")
						.param("email", "user1@test.com"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("signup_form"))
				.andExpect(model().attributeHasFieldErrorCode("userCreateForm", "password2", "passwordInCorrect"))
				.andExpect(content().string(containsString("2개의 패스워드가 일치하지 않습니다.")));

		assertThat(userRepository.count()).isZero();
	}

	@Test
	@DisplayName("회원가입: 이미 있는 사용자ID면 전역 오류 \"이미 등록된 사용자입니다.\"")
	void t4() throws Exception {
		userService.create("user1", "user1@test.com", "1234");

		mvc
				.perform(post("/user/signup").with(csrf())
						.param("username", "user1")
						.param("password1", "1234")
						.param("password2", "1234")
						.param("email", "other@test.com"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("signup_form"))
				.andExpect(model().attributeErrorCount("userCreateForm", 1))
				.andExpect(content().string(containsString("이미 등록된 사용자입니다.")));
	}
}
