package me.hjhng125.taxiallocationapi.taxi.request;


import me.hjhng125.taxiallocationapi.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaxiRequestRepositoryCustom {

    Page<TaxiRequestDTO> selectTaxiRequest(Member member, Pageable pageable);
}
