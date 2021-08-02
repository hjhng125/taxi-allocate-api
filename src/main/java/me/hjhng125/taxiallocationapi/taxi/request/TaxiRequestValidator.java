package me.hjhng125.taxiallocationapi.taxi.request;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import lombok.RequiredArgsConstructor;
import me.hjhng125.taxiallocationapi.exception.UserGuideException;
import me.hjhng125.taxiallocationapi.exception.UserGuideMessage;
import me.hjhng125.taxiallocationapi.member.Member;
import me.hjhng125.taxiallocationapi.member.MemberType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaxiRequestValidator {

    private final TaxiRequestRepository taxiRequests;

    public void validateCreateRequest(Member member, String address) {
        if (address.length() > 100) {
            throw new UserGuideException(BAD_REQUEST, UserGuideMessage.INVALID_ADDRESS_LENGTH);
        }

        if (member.getMemberType() == MemberType.DRIVER) {
            throw new UserGuideException(FORBIDDEN, UserGuideMessage.IMPOSSIBLE_DRIVER_CREATE_TAXI_REQUEST);
        }

        boolean exists = taxiRequests.existsByPassengerId(member.getId());
        if (exists) {
            throw new UserGuideException(CONFLICT, UserGuideMessage.ALREADY_EXISTS_REQUEST);
        }
    }
}
