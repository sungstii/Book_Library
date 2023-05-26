package server.book_library.domain.book.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import server.book_library.domain.library.inventory.entity.LibraryInventory;
import server.book_library.util.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Book extends BaseEntity {
    private String name;
    private String writer;
    private String publisher;
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties("book")
    private List<LibraryInventory> libraryInventories = new ArrayList<>();
}
