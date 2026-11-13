package kr.swdl.foodingredientmanagement.security;

import kr.swdl.foodingredientmanagement.entity.Member;
import kr.swdl.foodingredientmanagement.member.repository.JPAMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailService implements UserDetailsService {
    private final JPAMemberRepository jpaMemberRepository;


    @Override
    public UserDetails loadUserByUsername(String id)
            throws UsernameNotFoundException {

        Member member = jpaMemberRepository.findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("회원을 찾을 수 없습니다.")
                );

        return new AppUserDetails(member);
    }
}
