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

    public TaxiRequestDTO createRequest(Member member, TaxiRequestCreateDTO createDTO) {

        validator.validateCreateRequest(member, createDTO);
        TaxiRequest save = taxiRequests.save(TaxiRequest.builder()
            .passenger(member)
            .address(createDTO.getAddress())
            .build());

        return save.toDTO();
    }

    public TaxiRequestDTO acceptRequest(Member member, TaxiRequest taxiRequest) {

        validator.validateAcceptRequest(member, taxiRequest);

        taxiRequest.acceptByDriver(member);
        return taxiRequest.toDTO();
    }
}
