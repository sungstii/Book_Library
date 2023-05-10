package server.book_library.library.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import server.book_library.library.library.dto.LibraryDto;
import server.book_library.library.library.entity.Library;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LibraryMapper {
    Library libraryPostToLibrary(LibraryDto.Post post);
    LibraryDto.Response libraryToLibraryResponse(Library library);
}
