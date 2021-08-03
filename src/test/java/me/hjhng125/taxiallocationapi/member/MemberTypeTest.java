package me.hjhng125.taxiallocationapi.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import me.hjhng125.taxiallocationapi.exception.UserGuideException;
import me.hjhng125.taxiallocationapi.exception.UserGuideMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

class MemberTypeTest {

    @MethodSource
    @ParameterizedTest
    void member_type_test(String memberTypeTitle, MemberType expected, boolean isEquals) {
        MemberType actual = MemberType.getMemberType(memberTypeTitle);

        assertThat(actual == expected).isEqualTo(isEquals);
    }

    private static Stream<Arguments> member_type_test() {
        return Stream.of(
            Arguments.of("driver", MemberType.DRIVER, true),
            Arguments.of("passenger", MemberType.PASSENGER, true),
            Arguments.of("driver", MemberType.PASSENGER, false),
            Arguments.of("passenger", MemberType.DRIVER, false)
        );
    }

    @Test
    void assert_throw_test() {

        UserGuideException userGuideException = assertThrows(UserGuideException.class, () -> MemberType.getMemberType("admin"));

        assertThat(userGuideException.getMessage()).isEqualTo(UserGuideMessage.INVALID_MEMBER_TYPE.getUserGuideMessage());
        assertThat(userGuideException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}