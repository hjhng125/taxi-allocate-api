package me.hjhng125.taxiallocationapi.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import me.hjhng125.taxiallocationapi.exception.UserGuideException;
import me.hjhng125.taxiallocationapi.exception.UserGuideMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MemberService.class)
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @MockBean
    MemberRepository members;
    @MockBean
    PasswordEncoder passwordEncoder;

    String email;

    @BeforeEach
    void beforeEach() {
        email = "hjhng125@nate.com";
    }

    @Test
    void loadUserByUsername_not_exists_user_test() {
        when(members.findByEmail(email)).thenReturn(Optional.empty());

        UsernameNotFoundException usernameNotFoundException = assertThrows(UsernameNotFoundException.class, () -> memberService.loadUserByUsername(email));
        assertThat(usernameNotFoundException.getMessage()).isEqualTo(UserGuideMessage.USER_NOT_FOUND.getUserGuideMessage());

    }

    @Test
    void save_duplicated_email_test() {
        when(members.findByEmail(email)).thenReturn(
            Optional.of(Member.builder()
                .email(email)
                .build()));

        UserGuideException userGuideException = assertThrows(UserGuideException.class,
            () -> memberService.save(MemberCreateRequestDTO.builder()
                .email(email)
                .build()));

        assertThat(userGuideException.getHttpStatus()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(userGuideException.getMessage()).isEqualTo(UserGuideMessage.ALREADY_EXIST_MEMBER.getUserGuideMessage());
    }

}