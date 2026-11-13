package com.mysite.sbb.answer;

import jakarta.validation.constraints.NotBlank;

public record AnswerForm(
		@NotBlank(message = "내용은 필수항목입니다.")
		String content
) {
}
