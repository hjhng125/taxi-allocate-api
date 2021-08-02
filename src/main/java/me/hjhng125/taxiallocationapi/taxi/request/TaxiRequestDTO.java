package me.hjhng125.taxiallocationapi.taxi.request;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class TaxiRequestDTO {

    private final Long id;
    private final String address;
    private final Long driverId;
    private final Long passengerId;
    private final TaxiRequestStatus status;
    private final LocalDateTime acceptedAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @QueryProjection
    public TaxiRequestDTO(Long id, String address, Long driverId, Long passengerId, TaxiRequestStatus status, LocalDateTime acceptedAt, LocalDateTime createdAt,
        LocalDateTime updatedAt) {
        this.id = id;
        this.address = address;
        this.driverId = driverId;
        this.passengerId = passengerId;
        this.status = status;
        this.acceptedAt = acceptedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
