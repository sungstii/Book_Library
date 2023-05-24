package server.book_library.domain.library.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import server.book_library.domain.book.dto.BookDto;
import server.book_library.domain.library.inventory.entity.LibraryInventory;
import server.book_library.domain.library.library.dto.LibraryDto;

public class LibraryInventoryDto {
    @Getter
    @AllArgsConstructor
    public static class Post{
        private long bookId;
        private long libraryId;
        private int totalQuantity;
    }

    @Getter
    @AllArgsConstructor
    public static class Patch{
        private int totalQuantity;
    }
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private long id;
        private BookDto.Response book;
        private LibraryDto.Response library;
        private int totalQuantity;
        private int loanQuantity;
        private LibraryInventory.LoanStatus loanStatus;
    }

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LibraryResponse {
        private long id;
        private BookDto.Response book;
        private int totalQuantity;
        private int loanQuantity;
        private LibraryInventory.LoanStatus loanStatus;
    }

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookResponse {
        private long id;
        private LibraryDto.Response library;
        private int totalQuantity;
        private int loanQuantity;
        private LibraryInventory.LoanStatus loanStatus;
    }
}
