package server.book_library.domain.library.inventory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import server.book_library.domain.library.inventory.dto.LibraryInventoryDto;
import server.book_library.domain.library.inventory.entity.LibraryInventory;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LibraryInventoryMapper {
    LibraryInventory LibraryInventoryPostToLibraryInventory(LibraryInventoryDto.Post post);
    LibraryInventory LibraryInventoryPatchToLibraryInventory(LibraryInventoryDto.Patch patch);
    LibraryInventoryDto.Response LibraryInventoryToLibraryInventoryResponse(LibraryInventory libraryInventory);

}
