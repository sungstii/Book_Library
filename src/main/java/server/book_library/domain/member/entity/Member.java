package server.book_library.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import server.book_library.domain.loan.entity.Loan;
import server.book_library.util.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member extends BaseEntity {
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private String phone;
    private boolean isDeleted = false;
    private boolean overDue = false;
    private LocalDateTime penaltyDeadLine;

    @Enumerated(value = EnumType.STRING)
    private MemberStatus memberStatus = MemberStatus.ACTIVE;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Loan> loanBooks = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    public enum MemberStatus {
        ACTIVE,DELETE
    }
}
