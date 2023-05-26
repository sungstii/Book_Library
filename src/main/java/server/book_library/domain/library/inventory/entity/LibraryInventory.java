package server.book_library.domain.library.inventory.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import server.book_library.domain.book.entity.Book;
import server.book_library.domain.library.library.entity.Library;
import server.book_library.domain.loan.entity.Loan;
import server.book_library.util.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class LibraryInventory extends BaseEntity {
    private int totalQuantity;
    private boolean isDeleted = false;

    @ColumnDefault(value = "0")
    private int loanQuantity;

    @Enumerated(value = EnumType.STRING)
    private LoanStatus loanStatus = LoanStatus.대여가능;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonIgnoreProperties("libraryInventories")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "library_id")
    @JsonIgnoreProperties("libraryInventories")
    private Library library;

    @OneToMany(mappedBy = "libraryInventory", cascade = CascadeType.REMOVE)
    @JsonManagedReference(value = "loan_libraryInventory")
    private List<Loan> loans = new ArrayList<>();

    public enum LoanStatus{
        대여가능, 모두대여중
    }
}
