package server.book_library.config.security.auths.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
@Getter
public class LoginDto {
        private String email;
        private String password;
}
