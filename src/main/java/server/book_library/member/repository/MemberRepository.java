package server.book_library.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.book_library.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
