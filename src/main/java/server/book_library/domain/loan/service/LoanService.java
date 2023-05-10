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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;

    @Value("${max_loan_day}")
    private long maxLoanDay;

    public Loan loanBook(Loan loan) {
        return loanRepository.save(loan);
    }

    public Loan returnBook(Loan loan) {
        loan.setReturnedAt(LocalDateTime.now());
        loan.setLoanStats(Loan.LoanStats.반납완료);
        return loanRepository.save(loan);
    }

    public Loan findById(long id) {
        Optional<Loan> optionalLoan = loanRepository.findById(id);

        return optionalLoan.orElseThrow(() -> new BusinessLogicException(ExceptionCode.LOAN_NOT_FOUND));
    }

    public boolean checkLateReturn(Loan loan) {
        //Todo: 빌린날짜,반납한날짜 비교해서 패널티 확인 메서드
        //Todo: 날짜 계산 메서드  값 패널티 끝나는 날짜 메서드

        LocalDateTime returnedAt = loan.getReturnedAt();
        LocalDateTime loanedAt = loan.getLoanedAt();
        long daysBetween = ChronoUnit.DAYS.between(returnedAt, loanedAt);
        if(isOverDue(daysBetween)) {
            setMemberPenalty(loan, daysBetween);
            return true;
        } else return false;

    }

    private boolean isOverDue(long daysBetween) {
        return daysBetween > maxLoanDay;
    }

    private void setMemberPenalty(Loan loan, long daysBetween) {
        Member member = loan.getMember();
        member.setOverDue(true);
        member.setPenaltyDeadLine(LocalDateTime.now().plusDays(daysBetween -maxLoanDay));
    }
}
