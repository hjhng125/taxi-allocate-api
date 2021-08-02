package me.hjhng125.taxiallocationapi.taxi.request;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.RequiredArgsConstructor;
import me.hjhng125.taxiallocationapi.exception.UserGuideException;
import me.hjhng125.taxiallocationapi.exception.UserGuideMessage;
import me.hjhng125.taxiallocationapi.member.Member;
import me.hjhng125.taxiallocationapi.member.MemberType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TaxiRequestValidator {

    private final TaxiRequestRepository taxiRequests;

    public void validateCreateRequest(Member member, TaxiRequestCreateDTO createDTO) {
        if (!StringUtils.hasText(createDTO.getAddress())) {
            throw new UserGuideException(BAD_REQUEST, UserGuideMessage.REQUIRED_ADDRESS);
        }

        if (createDTO.getAddress().length() > 100) {
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

    public void validateAcceptRequest(Member member, TaxiRequest taxiRequest) {
        if (member.getMemberType() == MemberType.PASSENGER) {
            throw new UserGuideException(FORBIDDEN, UserGuideMessage.IMPOSSIBLE_PASSENGER_ACCEPT_REQUEST);
        }

        if (taxiRequest == null) {
            throw new UserGuideException(NOT_FOUND, UserGuideMessage.TAXI_REQUEST_NOT_FOUND);
        }

        if (taxiRequest.getDriver() != null) {
            throw new UserGuideException(CONFLICT, UserGuideMessage.ALREADY_ACCEPT_REQUEST);
        }
    }
}
