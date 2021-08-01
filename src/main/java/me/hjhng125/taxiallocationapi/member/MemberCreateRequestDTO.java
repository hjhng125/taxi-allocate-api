package me.hjhng125.taxiallocationapi.member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberCreateRequestDTO {

    @Email(message = "올바른 이메일을 입력해주세요")
    @NotBlank(message = "이메일을 입력해주세요")
    private final String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private final String password;

    @NotBlank(message = "유저타입을 입력해주세요")
    private final String userType;
}
