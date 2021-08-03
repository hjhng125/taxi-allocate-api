package me.hjhng125.taxiallocationapi.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class MemberServiceIntegrationTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository members;

    @Autowired
    PasswordEncoder passwordEncoder;

    String email;

    @BeforeEach
    void beforeEach() {
        email = "hjhng125@nate.com";
    }

    @Test
    @Transactional
    void successfully_save_test() {
        MemberCreateRequestDTO build = MemberCreateRequestDTO.builder()
            .email(email)
            .password("pass")
            .userType(MemberType.PASSENGER.getTitle())
            .build();

        MemberCreateResponseDTO save = memberService.save(build);
        assertThat(save).isNotNull();
        assertThat(save.getEmail()).isEqualTo(email);
    }
}
