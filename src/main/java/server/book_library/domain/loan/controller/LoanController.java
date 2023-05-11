package server.book_library.domain.loan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.book_library.domain.loan.service.LoanService;
import server.book_library.domain.member.entity.Member;
import server.book_library.domain.library.inventory.entity.LibraryInventory;
import server.book_library.domain.library.inventory.service.LibraryInventoryService;
import server.book_library.domain.loan.dto.LoanDto;
import server.book_library.domain.loan.entity.Loan;
import server.book_library.domain.loan.mapper.LoanMapper;
import server.book_library.domain.member.service.MemberService;
import server.book_library.global.dto.SingleResponse;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class LoanController {
    private final MemberService memberService;
    private final LibraryInventoryService libraryInventoryService;
    private final LoanService loanService;
    private final LoanMapper loanMapper;

    @Transactional
    @PostMapping("/loan")
    public ResponseEntity<?> loanBook(@RequestBody LoanDto.Post post) {
        //회원 -> 도서관에 등록된 책(LibraryBook) 대여
        Member member = memberService.findById(post.getMemberId());
        memberService.validMemberStatus(member);
        //Todo: 사용자가 대여가능한 상태인지 확인
        // 대여 조건1 : 대여 중인책이 5권 이하 -> O
        // 대여 조건2 : 연체되어서 패널티 안받고 있는지 -> O
        // 대여 조건3 : 같은 도서는 최대 2권까지
        memberService.validLoanStatus(member);

        LibraryInventory libraryInventory = libraryInventoryService.findById(post.getLibraryInventoryId());
        //Todo: 도서관에 등록된 책이 대여가능 상태인지 확인 코드 필요 (재고수량 확인) -> O
        libraryInventoryService.validLoanStatus(libraryInventory);

        Loan loan = loanMapper.loanPostToLoan(post);
        loan.setLoanedAt(LocalDateTime.now()); //대여한시간
        loan.setMember(member);
        loan.setLibraryInventory(libraryInventory);

        loanService.validDuplicationLoan(loan);

        //Todo: 대여후 도서관에 등록된 책 재고수량 수정 -> O
        libraryInventoryService.plusLoanQuantity(libraryInventory);

        Loan loanBook = loanService.loanBook(loan);
        LoanDto.Response response = loanMapper.loanToLoanResponse(loanBook);
        //Todo: 이 요청에서 예외,오류 발생시 롤백 필요 -> O

        return new ResponseEntity<>(new SingleResponse<>(response), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/return/{loan-id}")
    public ResponseEntity<?> returnBook(@RequestBody LoanDto.Post post, @PathVariable("loan-id") long loanId) {
        Member member = memberService.findById(post.getMemberId());
        LibraryInventory libraryInventory = libraryInventoryService.findById(post.getLibraryInventoryId());
        Loan loan = loanService.findById(loanId);
        //Todo:
        // 조건0. 이미 반납 상태이면 반납 처리 되면 안됨
        // 조건1. 해당 회원이 빌린 책이 맞는지 확인 필요 -> 안맞을시 예외 오류 response ->
        // 조건2. 날짜 확인후 연체 여부확인 및 패널티 부여 (연체된 날짜만큼 대여 불가능) -> 정상작동 및 member 상태변경
        loanService.validReturn(loan);
        loanService.validMemberMatch(loan, post.getMemberId());
        loan.setReturnedAt(LocalDateTime.now());

        if(loanService.checkLateReturn(loan)) {
            loanService.setMemberPenalty(loan);
        }


        //Todo: 반납시 같이 변경 되여야하는 것들
        // Loan = returnedAt 시간기입, loanStatus 상태변경,

        // Todo: LibraryInventory = loanQuantity 변경, loanStatus 상태 확인후 변경
        libraryInventoryService.minusLoanQuantity(libraryInventory);
        Loan returnBook = loanService.returnBook(loan);
        LoanDto.Response response = loanMapper.loanToLoanResponse(returnBook);
        return new ResponseEntity<>(new SingleResponse<>(response), HttpStatus.OK);
    }
}