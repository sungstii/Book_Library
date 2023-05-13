package server.book_library.domain.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class BookDto {
    @Getter
    @AllArgsConstructor
    public static class Post{
        private String name;
        private String writer;
        private String publisher;
    }
    @Getter
    @AllArgsConstructor
    public static class Patch{
        private String name;
        private String writer;
        private String publisher;
    }
    @Getter
    @AllArgsConstructor
    public static class Response{
        private long id;
        private String name;
        private String writer;
        private String publisher;
    }
}
