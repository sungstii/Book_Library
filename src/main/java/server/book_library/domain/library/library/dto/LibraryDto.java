package server.book_library.domain.library.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class LibraryDto {
    @Getter
    @AllArgsConstructor
    public static class Post {
        private long id;
        private String name;
        private String img_url;
        private String location;

    }
    @Getter
    @AllArgsConstructor
    public static class Response {
        private long id;
        private String name;
        private String img_url;
        private String createdAt;
    }
}
