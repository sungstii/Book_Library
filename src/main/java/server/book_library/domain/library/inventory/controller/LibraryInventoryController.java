package server.book_library.domain.library.inventory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.book_library.domain.book.entity.Book;
import server.book_library.domain.book.service.BookService;
import server.book_library.domain.library.inventory.dto.LibraryInventoryDto;
import server.book_library.domain.library.inventory.entity.LibraryInventory;
import server.book_library.domain.library.inventory.mapper.LibraryInventoryMapper;
import server.book_library.domain.library.inventory.service.LibraryInventoryService;
import server.book_library.domain.library.library.entity.Library;
import server.book_library.domain.library.library.service.LibraryService;
import server.book_library.global.dto.SingleResponse;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/libraries/inventories")
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
        LibraryInventory registration = libraryInventoryService.createLibraryInventory(libraryInventory);

        LibraryInventoryDto.Response response = libraryInventoryMapper.LibraryInventoryToLibraryInventoryResponse(registration);
        return new ResponseEntity<>(new SingleResponse<>(response), HttpStatus.CREATED);
    }

    @Transactional
    @PatchMapping("/{library-inventory-id}")
    public ResponseEntity<?> modifyLibraryInventory(@PathVariable("library-inventory-id") long libraryInventoryId,
                                                    @RequestBody LibraryInventoryDto.Patch patch) {
        LibraryInventory libraryInventory = libraryInventoryMapper.LibraryInventoryPatchToLibraryInventory(patch);
        libraryInventory.setId(libraryInventoryId);
        LibraryInventory updateLibraryInventory = libraryInventoryService.updateLibraryInventory(libraryInventory);
        LibraryInventoryDto.Response response = libraryInventoryMapper.LibraryInventoryToLibraryInventoryResponse(updateLibraryInventory);

        return new ResponseEntity<>(new SingleResponse<>(response), HttpStatus.OK);
    }

    @DeleteMapping("/{library-inventory-id}")
    public ResponseEntity<?> removeLibraryInventory(@PathVariable("library-inventory-id") long libraryInventoryId) {
        LibraryInventory libraryInventory = libraryInventoryService.findById(libraryInventoryId);
        LibraryInventory deleteLibraryInventory = libraryInventoryService.deleteLibraryInventory(libraryInventory);
        LibraryInventoryDto.Response response = libraryInventoryMapper.LibraryInventoryToLibraryInventoryResponse(deleteLibraryInventory);

        return new ResponseEntity<>(new SingleResponse<>(response), HttpStatus.OK);
    }
}
