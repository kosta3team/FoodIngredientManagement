package kr.swdl.foodingredientmanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;
    private String id;
    private String password;
    private String email;
    private String name;

    @Enumerated(EnumType.STRING)
    private MemberRoleType role;
}
