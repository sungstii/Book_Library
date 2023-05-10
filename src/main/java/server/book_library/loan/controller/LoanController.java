package server.book_library.loan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.book_library.library.inventory.entity.LibraryInventory;
import server.book_library.library.inventory.service.LibraryInventoryService;
import server.book_library.loan.dto.LoanDto;
import server.book_library.loan.entity.Loan;
import server.book_library.loan.mapper.LoanMapper;
import server.book_library.loan.service.LoanService;
import server.book_library.member.entity.Member;
import server.book_library.member.service.MemberService;

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
        //회원-> 도서관에 등록된 책(LibraryBook) 대여
        Member member = memberService.findById(post.getMemberId());
        //Todo: 사용자 대출가능여부 확인 코드 필요
        LibraryInventory libraryInventory = libraryInventoryService.findById(post.getLibraryInventoryId());
        //Todo: 도서관에 등록된 책이 대여가능 상태인지 확인 코드 필요 (재고수량 확인)
        Loan loan = loanMapper.loanPostToLoan(post);

        loan.setLoanedAt(LocalDateTime.now());
        loan.setMember(member);
        loan.setLibraryInventory(libraryInventory);
        libraryInventoryService.setLoanQuantity(libraryInventory); // 대여한 재고 수정

        Loan loaned = loanService.loanBook(loan);
        LoanDto.Response response = loanMapper.loanToLoanResponse(loaned);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/return/{loan-id}")
    public ResponseEntity<?> returnBook(@RequestBody LoanDto.Post post, @PathVariable("loan-id") long loanId) {
        Member member = memberService.findById(post.getMemberId());
        LibraryInventory libraryInventory = libraryInventoryService.findById(post.getLibraryInventoryId());
        Loan loan = loanService.findById(loanId);
        //Todo: 해당 회원이 빌린 책이 맞는지 확인 필요
        //Todo: 날짜 확인후 연체 여부 및 패널티 부여
        Loan returnBook = loanService.returnBook(loan);
        LoanDto.Response response = loanMapper.loanToLoanResponse(returnBook);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}