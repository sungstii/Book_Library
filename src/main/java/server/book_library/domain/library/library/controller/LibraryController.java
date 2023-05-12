package server.book_library.domain.library.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.book_library.domain.library.library.entity.Library;
import server.book_library.domain.library.library.service.LibraryService;
import server.book_library.domain.library.library.dto.LibraryDto;
import server.book_library.domain.library.library.mapper.LibraryMapper;
import server.book_library.global.dto.MultiResponse;
import server.book_library.global.dto.SingleResponse;

import java.util.List;

@RestController
@RequestMapping("/libraries")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;
    private final LibraryMapper libraryMapper;
    
    @PostMapping
    public ResponseEntity<?> addLibrary(@RequestBody LibraryDto.Post post) {
        Library library = libraryMapper.libraryPostToLibrary(post);
        Library createdLibrary = libraryService.createLibrary(library);
        LibraryDto.Response response = libraryMapper.libraryToLibraryResponse(createdLibrary);

        return new ResponseEntity<>(new SingleResponse<>(response), HttpStatus.CREATED);
    }

    @PatchMapping("/{library-id}")
    public ResponseEntity<?> modifyLibrary(@PathVariable("library-id") long libraryId,
                                           @RequestBody LibraryDto.Patch patch) {
        Library library = libraryMapper.libraryPatchToLibrary(patch);
        library.setId(libraryId);
        Library patchLibrary = libraryService.updateLibrary(library);
        LibraryDto.Response response = libraryMapper.libraryToLibraryResponse(patchLibrary);

        return new ResponseEntity<>(new SingleResponse<>(response), HttpStatus.OK);
    }

    @GetMapping("/{library-id}")
    public ResponseEntity<?> getLibrary(@PathVariable("library-id") long libraryId) {
        Library library = libraryService.findById(libraryId);
        LibraryDto.DetailResponse response = libraryMapper.libraryToLibraryDetailResponse(library);

        return new ResponseEntity<>(new SingleResponse<>(response), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getsLibraries(@RequestParam int page,
                                           @RequestParam int size) {
        Page<Library> librariesPages = libraryService.getLibraries(page, size);
        List<Library> libraries = librariesPages.getContent();
        List<LibraryDto.Response> responses = libraryMapper.librariesToLibraryResponses(libraries);

        return new ResponseEntity<>(new MultiResponse<>(responses, librariesPages), HttpStatus.OK);
    }

    @DeleteMapping("/{library-id}")
    public ResponseEntity<?> removeLibrary(@PathVariable("library-id") long libraryId) {
        Library library = libraryService.findById(libraryId);
        Library deleteLibrary = libraryService.deleteLibrary(library);
        LibraryDto.Response response = libraryMapper.libraryToLibraryResponse(deleteLibrary);

        return new ResponseEntity<>(new SingleResponse<>(response), HttpStatus.OK);
    }
}
