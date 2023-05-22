package server.book_library.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import server.book_library.domain.member.entity.Member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class MemberDto {
    @Getter
    @AllArgsConstructor
    public static class Post {
        @NotBlank
        @Email
        private String email;

        @NotBlank(message = "이름은 공백이 아니여야 합니다.")
        private String name;

        @NotBlank(message = "패스워드를 입력해 주세요(최소 8자 최대 12자)")
        @Pattern(regexp = "[(a-zA-Z0-9)`~!@#\\$%\\^&*\\(\\)-_=\\+]{8,12}", message = "영문자와 숫자, !@#$%^&*()_+-=만 사용 가능합니다 ")
        private String password;

        @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$",
                message = "휴대폰 번호는 010으로 시작하는 11자리 숫자와 '-'로 구성되어야 합니다.")
        private String phone;
    }

//    @Getter
//    @AllArgsConstructor
//    public static class Patch{
//        private String name;
//        private String email;
//        private String password;
//        private String phone;
//    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private long id;
        private String name;
        private String email;
        private String phone;
        private Member.MemberStatus memberStatus;
    }
}
