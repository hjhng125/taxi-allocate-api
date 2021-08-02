package me.hjhng125.taxiallocationapi.taxi.request;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import me.hjhng125.taxiallocationapi.member.Member;
import me.hjhng125.taxiallocationapi.member.MemberType;
import me.hjhng125.taxiallocationapi.member.QMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

public class TaxiRequestRepositoryCustomImpl extends QuerydslRepositorySupport implements TaxiRequestRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QTaxiRequest taxiRequest = QTaxiRequest.taxiRequest;
    private final QMember passenger = QMember.member;
    private final QMember driver = new QMember("driver");

    public TaxiRequestRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(TaxiRequest.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<TaxiRequestDTO> selectTaxiRequest(Member member, Pageable pageable) {
        JPAQuery<TaxiRequestDTO> query = jpaQueryFactory.select(new QTaxiRequestDTO(
            taxiRequest.id,
            taxiRequest.address,
            driver.id,
            passenger.id,
            taxiRequest.status,
            taxiRequest.acceptedAt,
            taxiRequest.createdAt,
            taxiRequest.updatedAt
        ))
            .from(taxiRequest)
            .innerJoin(taxiRequest.passenger, passenger)
            .leftJoin(taxiRequest.driver, driver)
            .where(whereClause(member));

        List<TaxiRequestDTO> fetch = query
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(taxiRequest.id.desc())
            .fetch();

        return PageableExecutionUtils.getPage(fetch, pageable, query::fetchCount);
    }

    private BooleanExpression whereClause(Member member) {
        if (member.getMemberType() == null || isDriver(member.getMemberType())) {
            return null;
        }
        return taxiRequest.passenger.id.eq(member.getId());
    }

    private boolean isDriver(MemberType memberType) {
        return memberType == MemberType.DRIVER;
    }

}
