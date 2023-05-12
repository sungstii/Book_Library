package server.book_library.domain.library.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import server.book_library.domain.library.inventory.dto.LibraryInventoryDto;

import java.util.List;

public class LibraryDto {
    @Getter @Setter
    @AllArgsConstructor
    public static class Post {
        private String name;
        private String location;
    }

    @Getter
    @AllArgsConstructor
    public static class Patch{
        private String name;
        private String location;
    }

    @Getter @Setter
    @AllArgsConstructor
    public static class Response {
        private long id;
        private String name;
        private String location;
        private String createdAt;
    }

    @Getter @Setter
    @AllArgsConstructor
    public static class DetailResponse {
        private long id;
        private String name;
        private String location;
        private List<LibraryInventoryDto.LibraryResponse> libraryInventories;
    }
}
