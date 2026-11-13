package kr.swdl.foodingredientmanagement.member.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberJoinRequest {
    private String id;
    private String password;
    private String email;
    private String name;

}
