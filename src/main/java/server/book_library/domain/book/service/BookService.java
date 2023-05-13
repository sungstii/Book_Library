package server.book_library.domain.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import server.book_library.domain.book.entity.Book;
import server.book_library.domain.book.repository.BookRepository;
import server.book_library.global.exception.BusinessLogicException;
import server.book_library.global.exception.ExceptionCode;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Book book) {
        Book findBook = findById(book.getId());
        Optional.ofNullable(book.getName()).ifPresent(findBook::setName);
        Optional.ofNullable(book.getWriter()).ifPresent(findBook::setWriter);
        Optional.ofNullable(book.getPublisher()).ifPresent(findBook::setPublisher);

        return bookRepository.save(findBook);
    }

    public Page<Book> findAllBooks(int page, int size) {
        return bookRepository.findByIsDeletedFalse(PageRequest.of(page, size));
    }

    public Book deleteBook(Book book){
        book.setDeleted(true);
        return bookRepository.save(book);
    }

    public Book findById(long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        Book book = optionalBook.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BOOK_NOT_FOUND));

        if(book.isDeleted()) {
            throw new BusinessLogicException(ExceptionCode.BOOK_IS_DELETED);
        }
        else return book;
    }
}
