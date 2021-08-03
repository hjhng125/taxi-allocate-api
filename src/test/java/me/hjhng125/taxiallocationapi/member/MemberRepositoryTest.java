package me.hjhng125.taxiallocationapi.member;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import me.hjhng125.taxiallocationapi.config.QuerydslConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QuerydslConfig.class)
class MemberRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    public void beforeEach() {
        entityManager.persist(Member.builder()
            .email("hjhng125@nate.com")
            .password("pass")
            .memberType(MemberType.PASSENGER)
            .build());
    }

    @MethodSource
    @ParameterizedTest
    void findByEmail_test(String email, boolean expected) {
        boolean actual = memberRepository.findByEmail(email).isPresent();

        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> findByEmail_test() {
        return Stream.of(
            Arguments.of("hjhng125@nate.com", true),
            Arguments.of("hjhng125@gmail.com", false)
        );
    }
}