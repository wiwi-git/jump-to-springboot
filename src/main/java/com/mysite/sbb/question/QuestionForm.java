package com.mysite.sbb.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record QuestionForm(
		@NotBlank(message = "제목은 필수항목입니다.")
		@Size(max = 200)
		String subject,

		@NotBlank(message = "내용은 필수항목입니다.")
		String content
) {
}
