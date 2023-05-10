package server.book_library.library.inventory.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import server.book_library.book.entity.Book;
import server.book_library.library.library.entity.Library;
import server.book_library.util.BaseEntity;

import javax.persistence.*;

@Entity
@Getter @Setter
public class LibraryInventory extends BaseEntity {
    private int totalQuantity;

    @ColumnDefault(value = "0")
    private int loanQuantity;

    @Enumerated(value = EnumType.STRING)
    private LoanStatus loanStatus = LoanStatus.대여가능;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonBackReference
    private Book book;

    @ManyToOne
    @JoinColumn(name = "library_id")
    @JsonBackReference
    private Library library;

    public enum LoanStatus{
        대여가능, 모두대여중
    }
}
