package server.book_library.domain.member.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import server.book_library.domain.member.dto.MemberDto;
import server.book_library.domain.member.entity.Member;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {
    Member memberPostToMember(MemberDto.Post post);
    MemberDto.Response memberToMemberResponse(Member member);
}
