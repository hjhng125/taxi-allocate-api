package me.hjhng125.taxiallocationapi.taxi.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaxiRequestStatus {

    STANDBY("standBy"),
    ACCEPTED("accepted");

    private final String title;
}
