package server.book_library.domain.library.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import server.book_library.domain.library.library.dto.LibraryDto;
import server.book_library.domain.library.library.entity.Library;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LibraryMapper {
    Library libraryPostToLibrary(LibraryDto.Post post);
    Library libraryPatchToLibrary(LibraryDto.Patch patch);
    LibraryDto.Response libraryToLibraryResponse(Library library);
    LibraryDto.DetailResponse libraryToLibraryDetailResponse(Library library);
    List<LibraryDto.Response> librariesToLibraryResponses(List<Library> libraries);
}
