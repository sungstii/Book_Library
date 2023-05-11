package server.book_library.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import server.book_library.domain.member.entity.Member;
import server.book_library.domain.member.repository.MemberRepository;
import server.book_library.domain.member.service.MemberService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class Schedule {
    private final MemberRepository memberRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void setMemberPenalty() {
        log.info("스케쥴러 실행됨");
        List<Member> members = memberRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        LocalDate localDate = now.toLocalDate();
        for(Member member : members) {
            if(member.isOverDue()) {
                LocalDateTime penaltyDeadLine = member.getPenaltyDeadLine();
                LocalDate localDated = penaltyDeadLine.toLocalDate();

                if(localDate.equals(localDated)) {
                    member.setOverDue(false);
                    member.setPenaltyDeadLine(null);
                    log.info("패널티 종료");
                }
            }
        }
        memberRepository.saveAll(members);
        log.info("저장완료");
    }
}
