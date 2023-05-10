package server.book_library.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import server.book_library.library.inventory.dto.LibraryInventoryDto;
import server.book_library.loan.entity.Loan;
import server.book_library.member.dto.MemberDto;

import java.time.LocalDateTime;

public class LoanDto {
    @Getter
    @AllArgsConstructor
    public static class Post{
        private long memberId;
        private long libraryInventoryId;
    }
    @Getter
    @AllArgsConstructor
    public static class Response{
        private long id;
        private MemberDto.Response member;
        private LibraryInventoryDto.Response libraryInventory;
        private LocalDateTime loanedAt;
        private LocalDateTime returnAt;
        private Loan.LoanStats loanStats;
    }
}