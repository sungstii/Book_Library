package server.book_library.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import server.book_library.domain.member.entity.Member;

public class MemberDto {
    @Getter
    @AllArgsConstructor
    public static class Post {
        private String name;
        private String email;
        private String password;
        private String phone;
    }

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
