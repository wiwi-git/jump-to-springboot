package com.mysite.sbb.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateForm(
		@Size(min = 3, max = 25)
		@NotBlank(message = "사용자ID는 필수항목입니다.")
		String username,

		@NotBlank(message = "비밀번호는 필수항목입니다.")
		String password1,

		@NotBlank(message = "비밀번호 확인은 필수항목입니다.")
		String password2,

		@NotBlank(message = "이메일은 필수항목입니다.")
		@Email
		String email
) {
}
