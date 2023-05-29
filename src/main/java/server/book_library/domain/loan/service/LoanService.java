package server.book_library.domain.loan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import server.book_library.domain.loan.repository.LoanRepository;
import server.book_library.domain.member.entity.Member;
import server.book_library.global.exception.BusinessLogicException;
import server.book_library.global.exception.ExceptionCode;
import server.book_library.domain.loan.entity.Loan;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;

    @Value("${max_loan_day}")
    private long maxLoanDay;

    @Value("${loan_limited}")
    private long loanLimit;

    public Loan loanBook(Loan loan) {
        return loanRepository.save(loan);
    }

    public Loan returnBook(Loan loan) {
        loan.setReturnedAt(LocalDateTime.now());
        loan.setLoanStats(Loan.LoanStats.반납완료);
        return loanRepository.save(loan);
    }

    public List<Loan> findByLibraryInventoryId(long libraryInventoryId) {
        return loanRepository.findByLibraryInventoryId(libraryInventoryId);
    }

    public Loan findById(long id) {
        Optional<Loan> optionalLoan = loanRepository.findById(id);

        return optionalLoan.orElseThrow(() -> new BusinessLogicException(ExceptionCode.LOAN_NOT_FOUND));
    }

    public void repayment(List<Loan> loans) {
        for (Loan loan : loans) {
            loan.setReturnedAt(LocalDateTime.now());
            loan.setLoanStats(Loan.LoanStats.반납완료);
        }
    }

    public void validReturn(Loan loan) {
        if (loan.getLoanStats().equals(Loan.LoanStats.반납완료)) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_RETURNED_BOOKS);
        }
    }

    public boolean checkLateReturn(Loan loan) {
        //Todo: 빌린날짜,반납한날짜 비교해서 패널티 확인 메서드
        //Todo: 날짜 계산 메서드  값 패널티 끝나는 날짜 메서드

        LocalDateTime returnedAt = loan.getReturnedAt();
        LocalDateTime loanedAt = loan.getLoanedAt();
        long daysBetween = ChronoUnit.DAYS.between(loanedAt, returnedAt);

        return daysBetween > maxLoanDay;
    }

    public void setMemberPenalty(Loan loan) {
        Member member = loan.getMember();
        LocalDateTime returnedAt = loan.getReturnedAt();
        LocalDateTime loanedAt = loan.getLoanedAt();
        long daysBetween = ChronoUnit.DAYS.between(loanedAt, returnedAt);
        member.setOverDue(true);
        member.setPenaltyDeadLine(LocalDateTime.now().plusDays(daysBetween - maxLoanDay));
    }

    public void validMemberMatch(Loan loan, long memberId) {
        if (loan.getMember().getId() != memberId) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_MATCH);
        }
    }

    public void validDuplicationLoan(Loan loan) {
        Member member = loan.getMember();
        List<Loan> loanBooks = member.getLoanBooks();
        int count = 0;
        for (Loan loanedBook : loanBooks) {
            if (loanedBook.getLibraryInventory().getId().equals(loan.getLibraryInventory().getId())) {
                if (loanedBook.getLoanStats().equals(Loan.LoanStats.대여중)) {
                    count++;
                }
                if (count >= loanLimit) {
                    throw new BusinessLogicException(ExceptionCode.LOAN_QUANTITY_LIMIT);
                }
            }
        }
    }
}
