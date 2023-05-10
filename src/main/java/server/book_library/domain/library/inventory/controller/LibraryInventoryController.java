package server.book_library.domain.library.inventory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.book_library.domain.book.entity.Book;
import server.book_library.domain.book.service.BookService;
import server.book_library.domain.library.inventory.dto.LibraryInventoryDto;
import server.book_library.domain.library.inventory.entity.LibraryInventory;
import server.book_library.domain.library.inventory.mapper.LibraryInventoryMapper;
import server.book_library.domain.library.inventory.service.LibraryInventoryService;
import server.book_library.domain.library.library.entity.Library;
import server.book_library.domain.library.library.service.LibraryService;
import server.book_library.global.dto.SingleResponse;

@RestController
@RequestMapping("/library/inventory")
@RequiredArgsConstructor
public class LibraryInventoryController {
    private final LibraryInventoryMapper libraryInventoryMapper;
    private final LibraryInventoryService libraryInventoryService;
    private final LibraryService libraryService;
    private final BookService bookService;
    @PostMapping
    public ResponseEntity<?> RegistrationInLibrary(@RequestBody LibraryInventoryDto.Post post) {
        Library library = libraryService.findById(post.getLibraryId());
        Book book = bookService.findById(post.getBookId());
        LibraryInventory libraryInventory = libraryInventoryMapper.LibraryInventoryPostToLibraryInventory(post);

        libraryInventory.setLibrary(library);
        libraryInventory.setBook(book);
        LibraryInventory registration = libraryInventoryService.registrationInLibrary(libraryInventory);

        LibraryInventoryDto.Response response = libraryInventoryMapper.LibraryInventoryToLibraryInventoryResponse(registration);
        return new ResponseEntity<>(new SingleResponse<>(response), HttpStatus.OK);
    }
}
