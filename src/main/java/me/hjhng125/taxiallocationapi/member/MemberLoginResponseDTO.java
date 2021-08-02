package me.hjhng125.taxiallocationapi.member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberLoginResponseDTO {
    private final String accessToken;
}
