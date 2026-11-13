package com.mysite.sbb.question;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.mysite.sbb.DataNotFoundException;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class QuestionControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private QuestionRepository questionRepository;

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
				.perform(post("/question/create")
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
				.perform(post("/question/create")
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
				.perform(post("/question/create")
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
				.perform(get("/question/create"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("question_form"))
				.andExpect(content().string(containsString("name=\"subject\" value=\"\"")));

		mvc
				.perform(post("/question/create")
						.param("subject", "제목만 입력")
						.param("content", ""))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name("question_form"))
				.andExpect(content().string(containsString("value=\"제목만 입력\"")));
	}
}
