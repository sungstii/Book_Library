package server.book_library.loan.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import server.book_library.loan.dto.LoanDto;
import server.book_library.loan.entity.Loan;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LoanMapper {
    Loan loanPostToLoan(LoanDto.Post post);
    LoanDto.Response loanToLoanResponse(Loan loan);
}
