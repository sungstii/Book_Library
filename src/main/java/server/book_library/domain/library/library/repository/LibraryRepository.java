package server.book_library.domain.library.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.book_library.domain.library.library.entity.Library;

@Repository
public interface LibraryRepository extends JpaRepository<Library, Long> {
    Page<Library> findByIsDeletedFalse(Pageable pageable);
}
