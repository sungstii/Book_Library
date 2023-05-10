package server.book_library.library.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.book_library.library.library.entity.Library;
import server.book_library.library.library.repository.LibraryRepository;

@Service
@RequiredArgsConstructor
public class LibraryService {
    private final LibraryRepository libraryRepository;

    public Library createLibrary(Library library) {

        return libraryRepository.save(library);
    }
}
