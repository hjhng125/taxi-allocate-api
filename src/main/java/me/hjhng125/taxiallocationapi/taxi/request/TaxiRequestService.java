package me.hjhng125.taxiallocationapi.taxi.request;

import lombok.RequiredArgsConstructor;
import me.hjhng125.taxiallocationapi.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaxiRequestService {

    private final TaxiRequestRepository taxiRequests;
    private final TaxiRequestValidator validator;

    public Page<TaxiRequestDTO> requests(Member member, Pageable pageable) {
        return taxiRequests.selectTaxiRequest(member, pageable);
    }

    public TaxiRequestDTO createRequest(Member member, String address) {

        validator.validateCreateRequest(member, address);
        TaxiRequest save = taxiRequests.save(TaxiRequest.builder()
            .passenger(member)
            .address(address)
            .build());

        return save.toDTO();
    }
}
