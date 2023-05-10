package server.book_library.domain.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.book_library.domain.book.dto.BookDto;
import server.book_library.domain.book.entity.Book;
import server.book_library.domain.book.mapper.BookMapper;
import server.book_library.domain.book.service.BookService;
import server.book_library.global.dto.SingleResponse;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final BookMapper bookMapper;

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody BookDto.Post post) {
        Book book = bookMapper.bookPostToBook(post);
        Book createdBook = bookService.createBook(book);
        BookDto.Response response = bookMapper.bookToBookResponse(createdBook);

        return new ResponseEntity<>(new SingleResponse<>(response), HttpStatus.CREATED);
    }
}