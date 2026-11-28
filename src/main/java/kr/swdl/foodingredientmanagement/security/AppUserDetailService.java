
package kr.swdl.foodingredientmanagement.security;

import kr.swdl.foodingredientmanagement.repository.UserRepository;
import kr.swdl.foodingredientmanagement.repository.entitiy.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId)
            throws UsernameNotFoundException {

        try {
            Long id = Long.valueOf(userId);

            Users users = userRepository.findById(id)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("회원을 찾을 수 없습니다.")
                    );

            // 사용자 정보 로그
            log.info("========== 로그인 사용자 정보 ==========");
            log.info("회원 ID : {}", users.getUserId());
            log.info("회원 이름 : {}", users.getUserName());
            log.info("회원 역할(DB) : {}", users.getRole());
            log.info("담당 매장 ID : {}", users.getStoreId());

            // Spring Security 사용자 객체 생성
            AppUserDetails userDetails = new AppUserDetails(users);

            // 실제 등록된 권한 확인
            log.info("Spring Security 권한 : {}", userDetails.getAuthorities());

            log.info("====================================");

            return userDetails;

        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("올바르지 않은 회원 ID입니다.");
        }
    }
}
