package server.book_library.security.auths.userdetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import server.book_library.domain.member.entity.Member;
import server.book_library.domain.member.repository.MemberRepository;
import server.book_library.global.exception.BusinessLogicException;
import server.book_library.global.exception.ExceptionCode;
import server.book_library.security.auths.utils.CustomAuthorityUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@Component
public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final CustomAuthorityUtils authorityUtils;

    public MemberDetailsService(MemberRepository memberRepository, CustomAuthorityUtils authorityUtils) {
        this.memberRepository = memberRepository;
        this.authorityUtils = authorityUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> memberByEmail = memberRepository.findByEmail(username);
        Member findMember = memberByEmail.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        return new MemberDetails(findMember);
    }

    private final class MemberDetails extends Member implements UserDetails {
        MemberDetails(Member member) {
            setId(member.getId());
            setEmail(member.getEmail());
            setPassword(member.getPassword());
            setRoles(member.getRoles());
            setName(member.getName());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities(){
            return authorityUtils.createAuthorities(getRoles());
        }

        @Override
        public String getUsername(){
            return getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
