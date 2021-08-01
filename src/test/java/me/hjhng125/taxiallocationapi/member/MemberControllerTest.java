package me.hjhng125.taxiallocationapi.member;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberService service;

    @MethodSource
    @ParameterizedTest
    void memberCreateRequestDTO_validate_test(String email, String password, String userType, String errorMessage) throws Exception {
        MemberCreateRequestDTO createRequestDTO = MemberCreateRequestDTO.builder()
            .email(email)
            .password(password)
            .userType(userType)
            .build();

        mockMvc.perform(post("/users/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequestDTO)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("message").value(errorMessage))
        ;
    }

    private static Stream<Arguments> memberCreateRequestDTO_validate_test() {
        return Stream.of(
            Arguments.of(null, "pass", MemberType.DRIVER.getTitle(), "이메일을 입력해주세요"),
            Arguments.of("hjhng125", "pass", MemberType.DRIVER.getTitle(), "올바른 이메일을 입력해주세요"),
            Arguments.of("hjhng125@nate.com", "", MemberType.DRIVER.getTitle(), "비밀번호를 입력해주세요"),
            Arguments.of("hjhng125@nate.com", "pass", "", "유저타입을 입력해주세요")
        );
    }

    @Test
    void member_sign_up_test() throws Exception {
        String email = "hjhng125@nate.com";
        String password = "password";
        String userType = MemberType.PASSENGER.getTitle();

        MemberCreateRequestDTO createRequestDTO = MemberCreateRequestDTO.builder()
            .email(email)
            .password(password)
            .userType(userType)
            .build();

        mockMvc.perform(post("/users/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequestDTO)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("email").value(email))
            .andExpect(jsonPath("userType").value(userType))
            .andExpect(jsonPath("createdAt").exists())
            .andExpect(jsonPath("updatedAt").exists())
        ;
    }
}