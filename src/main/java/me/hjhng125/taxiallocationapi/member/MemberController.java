package me.hjhng125.taxiallocationapi.member;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.hjhng125.taxiallocationapi.token.JwtTokenProvider;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/users/sign-up")
    public ResponseEntity<MemberCreateResponseDTO> signUp(@RequestBody @Valid MemberCreateRequestDTO createDTO) {

        WebMvcLinkBuilder selfLink = linkTo(methodOn(MemberController.class).signUp(createDTO));
        MemberCreateResponseDTO result = service.save(createDTO);

        return ResponseEntity.created(selfLink.toUri())
            .body(result);
    }

    @PostMapping("/users/sign-in")
    public ResponseEntity<MemberLoginResponseDTO> signIn(@RequestBody @Valid MemberLoginRequestDTO loginRequestDTO) {
        WebMvcLinkBuilder selfLink = linkTo(methodOn(MemberController.class).signIn(loginRequestDTO));

        Member member = service.matchByLoginInfo(loginRequestDTO);
        String token = jwtTokenProvider.createToken(member.getEmail());

        return ResponseEntity.created(selfLink.toUri())
            .body(MemberLoginResponseDTO.builder()
                .accessToken(token)
                .build());
    }
}
