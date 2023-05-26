package server.book_library.domain.library.library.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import server.book_library.domain.library.inventory.entity.LibraryInventory;
import server.book_library.domain.library.library.entity.Library;
import server.book_library.domain.library.library.repository.LibraryRepository;
import server.book_library.global.exception.BusinessLogicException;
import server.book_library.global.exception.ExceptionCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LibraryService {
    private final LibraryRepository libraryRepository;
    private final CacheManager cacheManager;

    @CachePut(value = "library", key = "#result.id", unless = "#result == null", cacheManager = "cacheManagerTest")
    public Library createLibrary(Library library) {
        Library save = libraryRepository.save(library);
        save.setLibraryInventories(new ArrayList<>());
        return save;
    }

    @CachePut(value = "library", key = "#result.id", unless = "#result == null", cacheManager = "cacheManagerTest")
    public Library updateLibrary(Library library) {
        Library findLibrary = findById(library.getId());
        Optional.ofNullable(library.getName()).ifPresent(findLibrary::setName);
        Optional.ofNullable(library.getLocation()).ifPresent(findLibrary::setLocation);

        Library updatedLibrary = libraryRepository.save(findLibrary);

        List<LibraryInventory> libraryInventories = new ArrayList<>(findLibrary.getLibraryInventories());

        for(LibraryInventory libraryInventory: libraryInventories) {
            libraryInventory.setBook(libraryInventory.getBook());
        }
        updatedLibrary.setLibraryInventories(libraryInventories);
        return updatedLibrary;
    }

    @Cacheable(value = "libraries", key = "#page + '-' + #size", cacheManager = "cacheManagerTest")
    public Page<Library> getLibraries(int page, int size) {
        return libraryRepository.findByIsDeletedFalse(PageRequest.of(page, size));
    }
    //libraries::0-5 -> 내용 Json


    // library::1 -> 내용 Json
    // library::2 -> 내용 Json
    @CacheEvict(value = "library", key = "#library.id")
    public Library deleteLibrary(Library library) {
        library.setDeleted(true);
        List<LibraryInventory> libraryInventories = library.getLibraryInventories();
        if(libraryInventories != null) {
            for (LibraryInventory libraryInventory : libraryInventories) {
                libraryInventory.setDeleted(true);
            }
        }
        evictAllLibraries();
        return libraryRepository.save(library);
    }

    @Cacheable(value = "library", key = "#id", unless = "#result == null", cacheManager = "cacheManagerTest")
    public Library findById(long id) {
        Optional<Library> optionalLibrary = libraryRepository.findById(id);
        Library library = optionalLibrary.orElseThrow(() -> new BusinessLogicException(ExceptionCode.LIBRARY_NOT_FOUND));

        List<LibraryInventory> libraryInventories = new ArrayList<>(library.getLibraryInventories());

        for(LibraryInventory libraryInventory: libraryInventories) {
            libraryInventory.setBook(libraryInventory.getBook());
        }
        library.setLibraryInventories(libraryInventories);

        if(library.isDeleted()) {
            throw new BusinessLogicException(ExceptionCode.LIBRARY_IS_GONE);
        }
        else return library;
    }

    public void evictAllLibraries() {
        Cache cache = cacheManager.getCache("libraries");
        if (cache != null) {
            cache.clear();
        }
    }
}
