package server.book_library.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.book_library.book.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
