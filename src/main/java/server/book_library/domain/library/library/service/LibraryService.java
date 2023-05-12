package server.book_library.domain.library.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import server.book_library.domain.library.library.entity.Library;
import server.book_library.domain.library.library.repository.LibraryRepository;
import server.book_library.global.exception.BusinessLogicException;
import server.book_library.global.exception.ExceptionCode;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibraryService {
    private final LibraryRepository libraryRepository;

    public Library createLibrary(Library library) {

        return libraryRepository.save(library);
    }

    public Library updateLibrary(Library library) {
        Library findLibrary = findById(library.getId());
        Optional.ofNullable(findLibrary.getName()).ifPresent(findLibrary::setName);
        Optional.ofNullable(findLibrary.getLocation()).ifPresent(findLibrary::setLocation);

        return libraryRepository.save(findLibrary);
    }

    public Page<Library> getLibraries(int page, int size) {
        return libraryRepository.findByIsDeletedFalse(PageRequest.of(page, size));
    }

    public Library deleteLibrary(Library library){
        library.setDeleted(true);
        return libraryRepository.save(library);
    }

    public Library findById(long id) {
        Optional<Library> optionalLibrary = libraryRepository.findById(id);
        Library library = optionalLibrary.orElseThrow(() -> new BusinessLogicException(ExceptionCode.LIBRARY_NOT_FOUND));

        if(library.isDeleted()) {
            throw new BusinessLogicException(ExceptionCode.LIBRARY_IS_GONE);
        }
        else return library;
    }
}
