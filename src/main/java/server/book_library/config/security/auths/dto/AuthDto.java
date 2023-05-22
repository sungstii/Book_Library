package server.book_library.config.security.auths.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class AuthDto {

    @Getter
    public class Login {
        @Email
        @NotBlank(message = "이메일을 확인해주세요")
        private String email;
        @NotBlank(message = "비밀번호를 확인해주세요")
        private String password;
    }
}
