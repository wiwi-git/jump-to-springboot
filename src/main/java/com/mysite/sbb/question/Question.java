package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 200)
	private String subject;

	@Column(columnDefinition = "TEXT")
	private String content;

	private LocalDateTime createDate;

	@OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private List<Answer> answerList = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	private SiteUser author;

	private LocalDateTime modifyDate;

	public Answer addAnswer(String content, SiteUser author) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setQuestion(this);
		answer.setAuthor(author);
		answer.setCreateDate(LocalDateTime.now());
		answerList.add(answer);

		return answer;
	}
}
