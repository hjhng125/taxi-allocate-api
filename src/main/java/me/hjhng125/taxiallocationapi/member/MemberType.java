package me.hjhng125.taxiallocationapi.member;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.hjhng125.taxiallocationapi.exception.UserGuideException;
import me.hjhng125.taxiallocationapi.exception.UserGuideMessage;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberType {

    PASSENGER("passenger"),
    DRIVER("driver");

    private final String title;

    public static MemberType getMemberType(String memberTypeTitle) {
        return Arrays.stream(MemberType.values())
            .filter(memberType -> memberType.title.equals(memberTypeTitle))
            .findFirst()
            .orElseThrow(() -> new UserGuideException(HttpStatus.BAD_REQUEST, UserGuideMessage.INVALID_MEMBER_TYPE));
    }
}
