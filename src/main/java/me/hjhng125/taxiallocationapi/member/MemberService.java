package me.hjhng125.taxiallocationapi.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hjhng125.taxiallocationapi.exception.UserGuideException;
import me.hjhng125.taxiallocationapi.exception.UserGuideMessage;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService implements UserDetailsService {

    private final MemberRepository members;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return members.findByEmail(email).orElseThrow(
            () -> new UsernameNotFoundException(UserGuideMessage.USER_NOT_FOUND.getUserGuideMessage()));
    }

    @Transactional
    public MemberCreateResponseDTO save(MemberCreateRequestDTO createDTO) {
        members.findByEmail(createDTO.getEmail())
            .ifPresent((present) -> {
                log.error("already present member: {}", present);
                throw new UserGuideException(HttpStatus.CONFLICT, UserGuideMessage.ALREADY_EXIST_MEMBER.getUserGuideMessage());
            });

        Member savedMember = members.save(Member.builder()
            .email(createDTO.getEmail())
            .password(passwordEncoder.encode(createDTO.getPassword()))
            .memberType(MemberType.getMemberType(createDTO.getUserType()))
            .build());

        return MemberCreateResponseDTO.builder()
            .id(savedMember.getId())
            .email(savedMember.getEmail())
            .userType(savedMember.getMemberType().getTitle())
            .createdAt(savedMember.getCreatedAt())
            .updatedAt(savedMember.getUpdatedAt())
            .build();
    }
}
