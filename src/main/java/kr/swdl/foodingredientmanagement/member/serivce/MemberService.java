package kr.swdl.foodingredientmanagement.member.serivce;

import kr.swdl.foodingredientmanagement.entity.Member;
import kr.swdl.foodingredientmanagement.entity.MemberRoleType;
import kr.swdl.foodingredientmanagement.member.dto.request.MemberJoinRequest;
import kr.swdl.foodingredientmanagement.member.repository.JPAMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final JPAMemberRepository jpaMemberRepository;
    private final PasswordEncoder passwordEncoder;

    public void join(MemberJoinRequest request){
        if(jpaMemberRepository.existsById(request.getId())){
            throw new IllegalStateException("이미 사용 중인 아이디입니다.");

        }
        Member member = new Member();
        member.setId(request.getId());
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member.setEmail(request.getEmail());
        member.setName(request.getName());
        member.setRole(MemberRoleType.ROLE_STORE);

        jpaMemberRepository.save(member);
   }
}
