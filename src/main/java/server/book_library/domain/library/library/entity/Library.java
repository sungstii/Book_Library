package server.book_library.domain.library.library.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import server.book_library.domain.library.inventory.entity.LibraryInventory;
import server.book_library.util.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter @Setter
public class Library extends BaseEntity {
    private String name;
    private String img_url;
    private String location;

    @OneToMany(mappedBy = "library", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<LibraryInventory> libraryInventories;
}
