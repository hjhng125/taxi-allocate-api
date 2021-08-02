package me.hjhng125.taxiallocationapi.taxi.request;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hjhng125.taxiallocationapi.exception.UserGuideMessage;
import me.hjhng125.taxiallocationapi.member.Member;
import me.hjhng125.taxiallocationapi.member.MemberRepository;
import me.hjhng125.taxiallocationapi.member.MemberType;
import me.hjhng125.taxiallocationapi.token.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
class TaxiRequestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtTokenProvider provider;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TaxiRequestRepository taxiRequestRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void taxi_request_unAuthorized_test() throws Exception {
        mockMvc.perform(get("/taxi-requests"))
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("message").value(UserGuideMessage.REQUIRED_LOGIN.getUserGuideMessage()));
    }

    @Test
    void taxi_request_invalid_token_test() throws Exception {
        String token = provider.createToken("hjhng125@nate.com");

        mockMvc.perform(get("/taxi-requests")
            .header("Authorization", "Bearer " + token))
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("message").value(UserGuideMessage.REQUIRED_LOGIN.getUserGuideMessage()));
    }

    @Test
    @Transactional
    void taxi_request_with_authorize_test() throws Exception {
        Member member = Member.builder()
            .email("hjhng125@nate.com")
            .password("pass")
            .memberType(MemberType.PASSENGER)
            .build();
        memberRepository.save(member);
        TaxiRequest taxiRequest = TaxiRequest.builder()
            .address("어디에 있을까")
            .passenger(member)
            .build();
        taxiRequestRepository.save(taxiRequest);
        String token = provider.createToken(member.getEmail());

        mockMvc.perform(get("/taxi-requests")
            .header("Authorization", "Token " + token))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("content").exists())
            .andExpect(jsonPath("content[0].id").exists())
            .andExpect(jsonPath("content[0].address").exists())
            .andExpect(jsonPath("content[0].driverId").doesNotExist())
            .andExpect(jsonPath("content[0].passengerId").exists())
            .andExpect(jsonPath("content[0].status").value(TaxiRequestStatus.STANDBY.name()))
            .andExpect(jsonPath("content[0].acceptedAt").doesNotExist())
            .andExpect(jsonPath("content[0].createdAt").exists())
            .andExpect(jsonPath("content[0].updatedAt").exists())
            .andExpect(jsonPath("pageable").exists())
        ;
    }

    @Test
    void create_request_test() throws Exception {

        Member member = Member.builder()
            .email("hjhng125@nate.com")
            .password("pass")
            .memberType(MemberType.PASSENGER)
            .build();
        memberRepository.save(member);
        String address = "캘리포니아 어딘가";
        String token = provider.createToken(member.getEmail());

        mockMvc.perform(post("/taxi-requests")
            .header("Authorization", "Token " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(address))
            .andDo(print())
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("address").exists())
            .andExpect(jsonPath("driverId").doesNotExist())
            .andExpect(jsonPath("passengerId").exists())
            .andExpect(jsonPath("status").value(TaxiRequestStatus.STANDBY.name()))
            .andExpect(jsonPath("acceptedAt").doesNotExist())
            .andExpect(jsonPath("createdAt").exists())
            .andExpect(jsonPath("updatedAt").exists())
        ;
    }
}