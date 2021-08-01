package me.hjhng125.taxiallocationapi.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

    private final String message;
}
