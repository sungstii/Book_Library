package server.book_library.domain.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.book_library.domain.book.dto.BookDto;
import server.book_library.domain.book.entity.Book;
import server.book_library.domain.book.mapper.BookMapper;
import server.book_library.domain.book.service.BookService;
import server.book_library.global.dto.MultiResponse;
import server.book_library.global.dto.SingleResponse;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final BookMapper bookMapper;

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody BookDto.Post post) {
        Book getBook = bookMapper.bookPostToBook(post);
        Book createdBook = bookService.createBook(getBook);
        BookDto.Response response = bookMapper.bookToBookResponse(createdBook);

        return new ResponseEntity<>(new SingleResponse<>(response), HttpStatus.CREATED);
    }

    @PatchMapping("/{book-id}")
    public ResponseEntity<?> updateBook(@PathVariable("book-id") long bookId,
                                        @RequestBody BookDto.Patch patch) {
        Book getBook = bookMapper.bookPatchToBook(patch);
        getBook.setId(bookId);
        Book updatedBook = bookService.patchBook(getBook);
        BookDto.Response response = bookMapper.bookToBookResponse(updatedBook);

        return new ResponseEntity<>(new SingleResponse<>(response), HttpStatus.OK);
    }

    @GetMapping("/{book-id}")
    public ResponseEntity<?> getBook(@PathVariable("book-id") long bookId){
        Book getBook = bookService.findById(bookId);
        BookDto.Response response = bookMapper.bookToBookResponse(getBook);

        return new ResponseEntity<>(new SingleResponse<>(response), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getBooks(@RequestParam int page,
                                      @RequestParam int size) {

        Page<Book> getBook = bookService.findAllBooks(page - 1, size);
        List<Book> books = getBook.getContent();
        List<BookDto.Response> responses = bookMapper.booksToBookResponse(books);

        return new ResponseEntity<>(new MultiResponse<>(responses, getBook), HttpStatus.OK);
    }

    @DeleteMapping("/{book-id}")
    public ResponseEntity<?> deleteBook(@PathVariable("book-id") long bookId) {
        Book getBook = bookService.findById(bookId);
        Book deleteBook = bookService.deleteBook(getBook);
        BookDto.Response response = bookMapper.bookToBookResponse(deleteBook);

        return new ResponseEntity<>(new SingleResponse<>(response), HttpStatus.OK);
    }
}