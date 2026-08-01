package com.arete.korbly.modules.sme.persistence;

import com.arete.korbly.modules.sme.domain.SME;
import com.arete.korbly.modules.sme.dto.RevenuePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SMERepository extends JpaRepository<SME, UUID> {
    @Query("select s from SME s where s.smeId = :smeId")
    Optional<SME> findSMEBySmeId(UUID smeId);

    @Query("select s from SME s where s.appUser.userId = :appUserId")
    Optional<SME> findByAppUserId(UUID appUserId);

    @Query("""
                select new com.arete.korbly.modules.sme.dto.RevenuePoint(
                    f.periodMonth,
                    f.revenue
                )
                from SmeMonthlyFinancials f
                where f.sme.smeId = :smeId
                  and f.periodMonth >= :fromMonth
                order by f.periodMonth asc
            """)
    List<RevenuePoint> revenueSeries(UUID smeId, LocalDate fromMonth);

    @Query("""
                select coalesce(avg(f.operatingCashFlow), 0)
                from SmeMonthlyFinancials f
                where f.sme.smeId = :smeId
                  and f.periodMonth >= :fromMonth
            """)
    BigDecimal avgOperatingCashflow(UUID smeId, LocalDate fromMonth);

    @Query("""
                select count(d.dealId)
                from Deal d
                where d.smeInvolved.smeId = :smeId
                  and d.dealStatus in ('DRAFT','OPEN','PUBLISHED','REVIEW')
            """)
    long countOpenApplications(UUID smeId);

    @Query("select s from SME s where s.appUser.userId = :appUserId")
    Optional<SME> findByAppUserUserId(UUID appUserId);

}
