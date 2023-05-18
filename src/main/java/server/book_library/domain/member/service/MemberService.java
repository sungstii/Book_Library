package server.book_library.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import server.book_library.domain.loan.entity.Loan;
import server.book_library.domain.member.entity.Member;
import server.book_library.domain.member.repository.MemberRepository;
import server.book_library.global.exception.BusinessLogicException;
import server.book_library.global.exception.ExceptionCode;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Value("${max_loan_quantity}")
    private long maxLoanQuantity;

    public Member createMember(Member member) {

        return memberRepository.save(member);
    }

    public Member deletedMember(Member member) {
        boolean hasActiveLoan = member.getLoanBooks().stream()
                .anyMatch(loan -> loan.getLoanStats().equals(Loan.LoanStats.대여중));

        if (hasActiveLoan) {
            throw new BusinessLogicException(ExceptionCode.UNABLE_MEMBER_ACCOUNT_WITHDRAWAL);
        }
        member.setMemberStatus(Member.MemberStatus.DELETE);
        return memberRepository.save(member);
    }

    public Member findById(long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        return optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    public Member findVerifiedEmail(String email) {
        Optional<Member> findMember = memberRepository.findByEmail(email);
        return findMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }


    public void validLoanQuantity(Member member) {
        //Todo: 사용자가 대여가능한 상태인지 확인
        // 대여 조건1 : 대여 중인책이 5권 이하
        // 대여 조건2 : 연체되어서 패널티 안받고 있는지 ( 이거는 LoanService 에서 구현 )
        if(isOverQuantity(member)) {
            throw new BusinessLogicException(ExceptionCode.EXCEEDS_MAXIMUM_QUANTITY);
        }
    }

    public void validLoanOverDue(Member member) {
        if(member.isOverDue()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_HAS_PENALTY);
        }
    }

    public void validLoanStatus(Member member) {
        validLoanOverDue(member);
        validLoanQuantity(member);
    }

    public boolean isOverQuantity(Member member) {
        List<Loan> loanBooks = member.getLoanBooks();
        long loanNum = 0;

        for(Loan loan : loanBooks) {
            Loan.LoanStats loanStats = loan.getLoanStats();
            if(loanStats.equals(Loan.LoanStats.대여중)) {
                loanNum ++;
            }
        }
        return loanNum >= maxLoanQuantity;
    }

    public void validMemberStatus(Member member) {
        if(member.getMemberStatus().equals(Member.MemberStatus.DELETE)) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_DELETED);
        }
    }
}