package me.hjhng125.taxiallocationapi.member;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberCreateResponseDTO {

    private final Long id;
    private final String email;
    private final String userType;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
