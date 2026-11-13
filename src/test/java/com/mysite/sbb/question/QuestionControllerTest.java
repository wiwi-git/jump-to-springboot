package com.mysite.sbb.question;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
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
}
