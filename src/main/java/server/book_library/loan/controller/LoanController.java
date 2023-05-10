package server.book_library.loan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("/loan")
@RequiredArgsConstructor
public class LoanController {
    private final MemberService memberService;
    private final LibraryInventoryService libraryInventoryService;
    private final LoanService loanService;
    private final LoanMapper loanMapper;
    @Transactional
    @PostMapping
    public ResponseEntity<?> loanBook(@RequestBody LoanDto.Post post) {
        Member member = memberService.findById(post.getMemberId());
        //Todo: 사용자 대출가능여부 확인 코드 필요
        LibraryInventory libraryInventory = libraryInventoryService.findById(post.getLibraryInventoryId());
        Loan loan = loanMapper.loanPostToLoan(post);
        loan.setLoanedAt(LocalDateTime.now());
        loan.setMember(member);
        loan.setLibraryInventory(libraryInventory);
        libraryInventoryService.setLoanQuantity(libraryInventory);
        Loan loaned = loanService.loanBook(loan);
        LoanDto.Response response = loanMapper.loanToLoanResponse(loaned);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}