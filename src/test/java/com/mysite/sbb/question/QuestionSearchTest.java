package com.mysite.sbb.question;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class QuestionSearchTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerService answerService;

	@Autowired
	private UserService userService;

	// 테스트 DB에는 TestInitData의 질문 2개(글쓴이 없음)가 있다.
	// 여기에 writer1이 쓴 "사과 질문"과, 그 질문에 replier1이 단 "바나나 답변" 2개를 더한다.
	@BeforeEach
	void setUp() {
		SiteUser writer = userService.create("writer1", "writer1@test.com", "1234");
		SiteUser replier = userService.create("replier1", "replier1@test.com", "1234");
		questionService.create("사과 질문", "과일 이야기", writer);
		Question question = questionRepository.findBySubject("사과 질문");
		answerService.create(question, "바나나 답변 하나", replier);
		answerService.create(question, "바나나 답변 둘", replier);
	}

	private Page<Question> search(String kw) {
		return questionService.getList(0, kw);
	}

	@Test
	@DisplayName("검색: 제목·내용·질문 글쓴이·답변 내용·답변 글쓴이 어느 것으로도 찾는다")
	void t1() {
		assertThat(search("사과").getContent()).extracting("subject").containsExactly("사과 질문"); // 제목
		assertThat(search("과일").getContent()).extracting("subject").containsExactly("사과 질문"); // 내용
		assertThat(search("writer1").getContent()).extracting("subject").containsExactly("사과 질문"); // 질문 글쓴이
		assertThat(search("바나나").getContent()).extracting("subject").containsExactly("사과 질문"); // 답변 내용
		assertThat(search("replier1").getContent()).extracting("subject").containsExactly("사과 질문"); // 답변 글쓴이
		assertThat(search("없는 말").getTotalElements()).isZero();
	}

	@Test
	@DisplayName("검색: 답변이 여러 개 걸려도 질문은 한 번만 센다(distinct)")
	void t2() {
		// "바나나"는 답변 2개에 모두 있어 join하면 질문 행이 2개가 된다.
		// 페이지 크기를 1로 하면 스프링 데이터가 count 쿼리로 전체 개수를 구하는데, distinct가 없으면 2가 된다
		Page<Question> page = questionRepository.findAllByKeyword("바나나", PageRequest.of(0, 1));
		assertThat(page.getTotalElements()).isEqualTo(1);
		assertThat(page.getTotalPages()).isEqualTo(1);
		assertThat(page.getContent()).extracting("subject").containsExactly("사과 질문");
	}

	@Test
	@DisplayName("검색: 검색어가 비어 있으면 모든 질문이 최신순으로 나온다(글쓴이 없는 질문 포함)")
	void t3() {
		Page<Question> page = search("");
		assertThat(page.getTotalElements()).isEqualTo(questionRepository.count());
		assertThat(page.getContent().get(0).getSubject()).isEqualTo("사과 질문");
	}

	@Test
	@DisplayName("검색 화면: 검색어가 검색창과 searchForm의 hidden kw에 유지된다")
	void t4() throws Exception {
		mvc
				.perform(get("/question/list").param("kw", "바나나").param("page", "0"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(model().attribute("kw", "바나나"))
				.andExpect(content().string(containsString("id=\"search_kw\" class=\"form-control\" value=\"바나나\"")))
				.andExpect(content().string(containsString("id=\"kw\" name=\"kw\" value=\"바나나\"")))
				.andExpect(content().string(containsString("사과 질문")))
				.andExpect(content().string(org.hamcrest.Matchers.not(containsString("스프링부트 모델 질문입니다."))));
	}
}
