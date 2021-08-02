package me.hjhng125.taxiallocationapi.taxi.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import me.hjhng125.taxiallocationapi.config.QuerydslConfig;
import me.hjhng125.taxiallocationapi.member.Member;
import me.hjhng125.taxiallocationapi.member.MemberType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@Import(QuerydslConfig.class)
class TaxiRequestRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    TaxiRequestRepository requests;

    Member member1, member2, member3;

    @BeforeEach
    void beforeEach() {
        member1 = Member.builder()
            .memberType(MemberType.PASSENGER)
            .build();
        member2 = Member.builder()
            .memberType(MemberType.PASSENGER)
            .build();
        member3 = Member.builder()
            .memberType(MemberType.DRIVER)
            .build();
        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);

        TaxiRequest request = TaxiRequest.builder()
            .passenger(member1)
            .address("경기도 성남시")
            .build();
        entityManager.persist(request);
        TaxiRequest request2 = TaxiRequest.builder()
            .passenger(member2)
            .address("서울특별시 강남구")
            .build();
        entityManager.persist(request2);

    }

    @Test
    void taxi_request_list() {

        Page<TaxiRequestDTO> taxiRequestDTOS = requests.selectTaxiRequest(member1, PageRequest.of(0, 10));

        for (TaxiRequestDTO taxiRequestDTO : taxiRequestDTOS) {
            System.out.println("taxiRequestDTO = " + taxiRequestDTO);
        }

        assertThat(taxiRequestDTOS.getContent().size()).isEqualTo(1);
        assertThat(taxiRequestDTOS).extracting("address").containsExactly("경기도 성남시");
        assertThat(taxiRequestDTOS).extracting("passengerId").containsExactly(1L);
    }

    @Test
    void taxi_request_driver_list() {

        Page<TaxiRequestDTO> taxiRequestDTOS = requests.selectTaxiRequest(member3, PageRequest.of(0, 10));

        for (TaxiRequestDTO taxiRequestDTO : taxiRequestDTOS) {
            System.out.println("taxiRequestDTO = " + taxiRequestDTO);
        }

        assertThat(taxiRequestDTOS.getContent().size()).isEqualTo(2);
        assertThat(taxiRequestDTOS).extracting("address").containsExactly("서울특별시 강남구", "경기도 성남시");
    }
}