package me.hjhng125.taxiallocationapi.taxi.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaxiRequestCreateDTO {

    private final String address;

    @JsonCreator // 인자가 하나인 경우 선언해줘야 jackson이 바인딩함.
    public TaxiRequestCreateDTO(String address) {
        this.address = address;
    }
}
