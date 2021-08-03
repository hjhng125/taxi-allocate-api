package me.hjhng125.taxiallocationapi.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class MemberTest {

    @Test
    void member_builder_test() {
        Member member = Member.builder()
            .email("hjhng125@nate.com")
            .password("password")
            .memberType(MemberType.PASSENGER)
            .build();
        assertThat(member).isNotNull();
    }
}
