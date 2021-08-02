package me.hjhng125.taxiallocationapi.taxi.request;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxiRequestRepository extends JpaRepository<TaxiRequest, Long>, TaxiRequestRepositoryCustom {

    boolean existsByPassengerId(Long passengerId);
}
