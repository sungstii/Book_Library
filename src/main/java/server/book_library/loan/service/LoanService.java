package server.book_library.loan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.book_library.loan.entity.Loan;
import server.book_library.loan.repository.LoanRepository;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;

    public Loan loanBook(Loan loan) {
        return loanRepository.save(loan);
    }
}
