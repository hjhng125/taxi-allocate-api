package me.hjhng125.taxiallocationapi.member;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;

    @PostMapping("/users/sign-up")
    public ResponseEntity<MemberCreateResponseDTO> signUp(@RequestBody @Valid MemberCreateRequestDTO createDTO) {

        WebMvcLinkBuilder selfLink = linkTo(methodOn(MemberController.class).signUp(createDTO));
        MemberCreateResponseDTO result = service.save(createDTO);

        return ResponseEntity.created(selfLink.toUri())
            .body(result);
    }
}
