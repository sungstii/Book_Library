package server.book_library.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.book_library.domain.member.dto.MemberDto;
import server.book_library.domain.member.entity.Member;
import server.book_library.domain.member.mapper.MemberMapper;
import server.book_library.domain.member.service.MemberService;
import server.book_library.global.dto.SingleResponse;


@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberMapper memberMapper;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> createMember(@RequestBody MemberDto.Post post) {
        Member member = memberMapper.memberPostToMember(post);
        Member createdMember = memberService.createMember(member);
        MemberDto.Response response = memberMapper.memberToMemberResponse(createdMember);

        return new ResponseEntity<>(new SingleResponse<>(response), HttpStatus.CREATED);
    }

    @DeleteMapping("{member-id}")
    public ResponseEntity<?> deleteMember(@PathVariable("member-id") long memberId) {
        Member member = memberService.findById(memberId);
        Member memberDelete = memberService.deletedMember(member);
        MemberDto.Response response = memberMapper.memberToMemberResponse(memberDelete);

        return new ResponseEntity<>(new SingleResponse<>(response), HttpStatus.OK);
    }
}
