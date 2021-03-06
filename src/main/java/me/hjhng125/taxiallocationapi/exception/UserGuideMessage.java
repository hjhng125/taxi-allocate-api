package me.hjhng125.taxiallocationapi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.logging.LogLevel;

@Getter
@RequiredArgsConstructor
public enum UserGuideMessage {

    REQUEST_SUCCESS(LogLevel.INFO, "요청이 성공했습니댜"),

    INVALID_MEMBER_TYPE(LogLevel.ERROR, "유저 타입이 유효하지 않습니다"),
    INVALID_EMAIL_PATTERN(LogLevel.ERROR, "올바른 이메일을 입력해주세요"),

    USER_NOT_FOUND(LogLevel.ERROR, "존재하지 않은 유저입니다"),
    ALREADY_EXIST_MEMBER(LogLevel.ERROR, "이미 가입된 이메일입니다"),
    INVALID_LOGIN_INFO(LogLevel.ERROR, "아이디와 비밀번호를 확인해주세요"),

    REQUIRED_LOGIN(LogLevel.ERROR, "로그인이 필요합니다"),

    IMPOSSIBLE_DRIVER_CREATE_TAXI_REQUEST(LogLevel.ERROR, "승객만 배차 요청할 수 있습니다"),
    REQUIRED_ADDRESS(LogLevel.ERROR, "주소를 입력해주세요"),
    INVALID_ADDRESS_LENGTH(LogLevel.ERROR, "주소는 100자 이하로 입력해주세요"),
    ALREADY_EXISTS_REQUEST(LogLevel.ERROR, "아직 대기중인 배차 요청이 있습니다"),

    IMPOSSIBLE_PASSENGER_ACCEPT_REQUEST(LogLevel.ERROR, "기사만 배차 요청을 수락할 수 있습니다"),
    TAXI_REQUEST_NOT_FOUND(LogLevel.ERROR, "존재하지 않는 배차 요청입니다"),
    ALREADY_ACCEPT_REQUEST(LogLevel.ERROR, "수락할 수 없는 배차 요청입니다. 다른 배차 요청을 선택하세요")

    ;

    private final LogLevel logLevel;
    private final String userGuideMessage;
}