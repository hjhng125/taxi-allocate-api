package me.hjhng125.taxiallocationapi.taxi.request;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.hjhng125.taxiallocationapi.common.BaseEntity;
import me.hjhng125.taxiallocationapi.member.Member;
import org.springframework.stereotype.Component;

@Entity
@Getter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PACKAGE)
public class TaxiRequest extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 100)
    private String address;

    @ManyToOne
    private Member driver;

    @ManyToOne(optional = false)
    private Member passenger;

    @Enumerated(STRING)
    @Builder.Default
    private TaxiRequestStatus status = TaxiRequestStatus.STANDBY;

    private LocalDateTime acceptedAt;

    public void acceptByDriver(Member driver) {
        this.driver = driver;
        this.status = TaxiRequestStatus.ACCEPTED;
        this.acceptedAt = LocalDateTime.now();
    }
}
