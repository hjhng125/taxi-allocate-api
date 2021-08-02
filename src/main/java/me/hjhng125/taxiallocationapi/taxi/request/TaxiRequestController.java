package me.hjhng125.taxiallocationapi.taxi.request;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hjhng125.taxiallocationapi.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TaxiRequestController {

    private final TaxiRequestService service;

    @GetMapping("/taxi-requests")
    public ResponseEntity<Page<TaxiRequestDTO>> list(@AuthenticationPrincipal Member member, @PageableDefault Pageable pageable) {
        Page<TaxiRequestDTO> requests = service.requests(member, pageable);
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/taxi-requests")
    public ResponseEntity<TaxiRequestDTO> request(@AuthenticationPrincipal Member member, @RequestBody String address) {

        TaxiRequestDTO result = service.createRequest(member, address);

        WebMvcLinkBuilder selfLink = linkTo(methodOn(TaxiRequestController.class).request(member, address));
        return ResponseEntity.created(selfLink.toUri())
            .body(result);
    }

}
