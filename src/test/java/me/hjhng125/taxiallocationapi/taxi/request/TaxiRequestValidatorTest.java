package me.hjhng125.taxiallocationapi.taxi.request;

import static me.hjhng125.taxiallocationapi.member.MemberType.DRIVER;
import static me.hjhng125.taxiallocationapi.member.MemberType.PASSENGER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import me.hjhng125.taxiallocationapi.exception.UserGuideException;
import me.hjhng125.taxiallocationapi.exception.UserGuideMessage;
import me.hjhng125.taxiallocationapi.member.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TaxiRequestValidator.class)
class TaxiRequestValidatorTest {

    @Autowired
    TaxiRequestValidator validator;

    @MockBean
    TaxiRequestRepository taxiRequests;

    @Test
    void validateCreateRequest_주소의_길이가_100_보다_크다() {
        String address = "123123swqeralfkjasflkasdhfjkhjkhiuosdahfuihsdm,fbnajkewbrjikwaehifjuhsadfjkasfbakjsdbhfuiwehfuiwehfskjdf";
        System.out.println("address.length() = " + address.length());
        UserGuideException userGuideException = assertThrows(UserGuideException.class, () -> validator.validateCreateRequest(Member.builder()
            .build(), TaxiRequestCreateDTO.builder()
            .address(address)
            .build()));

        assertThat(userGuideException.getHttpStatus()).isEqualTo(BAD_REQUEST);
        assertThat(userGuideException.getMessage()).isEqualTo(UserGuideMessage.INVALID_ADDRESS_LENGTH.getUserGuideMessage());
    }

    @Test
    void validateCreateRequest_주소가_없다() {
        String address = "";

        UserGuideException userGuideException = assertThrows(UserGuideException.class, () -> validator.validateCreateRequest(Member.builder()
            .build(), TaxiRequestCreateDTO.builder()
            .address(address)
            .build()));

        assertThat(userGuideException.getHttpStatus()).isEqualTo(BAD_REQUEST);
        assertThat(userGuideException.getMessage()).isEqualTo(UserGuideMessage.REQUIRED_ADDRESS.getUserGuideMessage());
    }

    @Test
    void validateCreateRequest_주소의_길이가_100_보다_크지_않다() {
        String address = "12312kasdhfjkhjkhiuosdahfuihsdm,fbnajkewbrjikwaehifjuhsadfjkasfbakjsdbhfuiwehfuiwehfskjdf";
        System.out.println("address.length() = " + address.length());

        assertDoesNotThrow(() -> validator.validateCreateRequest(Member.builder()
            .id(1L)
            .memberType(PASSENGER)
            .build(), TaxiRequestCreateDTO.builder()
            .address(address)
            .build()));
    }

    @Test
    void validateCreateRequest_주소의_길이는_100_보다_작고_승객이_아니다() {
        String address = "12312kasdhfjkhjkhiuosdahfuihsdm,fbnajkewbrjikwaehifjuhsadfjkasfbakjsdbhfuiwehfuiwehfskjdf";
        System.out.println("address.length() = " + address.length());

        UserGuideException userGuideException = assertThrows(UserGuideException.class, () -> validator.validateCreateRequest(Member.builder()
            .memberType(DRIVER)
            .build(), TaxiRequestCreateDTO.builder()
            .address(address)
            .build()));

        assertThat(userGuideException.getHttpStatus()).isEqualTo(FORBIDDEN);
        assertThat(userGuideException.getMessage()).isEqualTo(UserGuideMessage.IMPOSSIBLE_DRIVER_CREATE_TAXI_REQUEST.getUserGuideMessage());
    }

    @Test
    void validateCreateRequest_이미_요청을_했으면_에러() {
        String address = "12312kasdhfjkhjkhiuosdahfuihsdm,fbnajkewbrjikwaehifjuhsadfjkasfbakjsdbhfuiwehfuiwehfskjdf";
        System.out.println("address.length() = " + address.length());

        Member member = Member.builder()
            .id(1L)
            .memberType(PASSENGER)
            .build();

        when(taxiRequests.existsByPassengerId(member.getId())).thenReturn(true);

        UserGuideException userGuideException = assertThrows(UserGuideException.class, () -> {
            validator.validateCreateRequest(member, TaxiRequestCreateDTO.builder()
                .address(address)
                .build());
        });

        assertThat(userGuideException.getHttpStatus()).isEqualTo(CONFLICT);
        assertThat(userGuideException.getMessage()).isEqualTo(UserGuideMessage.ALREADY_EXISTS_REQUEST.getUserGuideMessage());
    }

    @Test
    void validateAcceptRequest_승객인_경우() {
        TaxiRequest request = TaxiRequest.builder()
            .build();

        UserGuideException userGuideException = assertThrows(UserGuideException.class, () -> validator.validateAcceptRequest(Member.builder()
            .memberType(PASSENGER)
            .build(), request));

        assertThat(userGuideException.getHttpStatus()).isEqualTo(FORBIDDEN);
        assertThat(userGuideException.getMessage()).isEqualTo(UserGuideMessage.IMPOSSIBLE_PASSENGER_ACCEPT_REQUEST.getUserGuideMessage());
    }

    @Test
    void validateAcceptRequest_요청이_없는_경우() {

        UserGuideException userGuideException = assertThrows(UserGuideException.class, () -> validator.validateAcceptRequest(Member.builder()
            .memberType(DRIVER)
            .build(), null));

        assertThat(userGuideException.getHttpStatus()).isEqualTo(NOT_FOUND);
        assertThat(userGuideException.getMessage()).isEqualTo(UserGuideMessage.TAXI_REQUEST_NOT_FOUND.getUserGuideMessage());
    }

    @Test
    void validateAcceptRequest_이미_승인된_요청인_경우() {
        TaxiRequest request = TaxiRequest.builder()
            .driver(Member.builder()
                .build())
            .build();

        UserGuideException userGuideException = assertThrows(UserGuideException.class, () -> validator.validateAcceptRequest(Member.builder()
            .memberType(DRIVER)
            .build(), request));

        assertThat(userGuideException.getHttpStatus()).isEqualTo(CONFLICT);
        assertThat(userGuideException.getMessage()).isEqualTo(UserGuideMessage.ALREADY_ACCEPT_REQUEST.getUserGuideMessage());
    }

    @Test
    void validateAcceptRequest_유효성_통과() {

        Member member = Member.builder()
            .memberType(DRIVER)
            .build();

        TaxiRequest taxiRequest = TaxiRequest.builder()
            .build();

        assertDoesNotThrow(() ->
            validator.validateAcceptRequest(member, taxiRequest)
        );

    }
}