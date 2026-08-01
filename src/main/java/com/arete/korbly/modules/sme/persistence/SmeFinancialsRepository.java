package com.arete.korbly.modules.sme.persistence;

import com.arete.korbly.modules.sme.domain.SmeMonthlyFinancials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SmeFinancialsRepository extends JpaRepository<SmeMonthlyFinancials, UUID> {
    @Query("""
        select f from SmeMonthlyFinancials f
        where f.sme.smeId = :smeId and f.periodMonth = :periodMonth
    """)
    Optional<SmeMonthlyFinancials> findOne(UUID smeId, LocalDate periodMonth);

    @Query("""
        select f from SmeMonthlyFinancials f
        where f.sme.smeId = :smeId and f.periodMonth between :fromMonth and :toMonth
        order by f.periodMonth asc
    """)
    List<SmeMonthlyFinancials> findRange(UUID smeId, LocalDate fromMonth, LocalDate toMonth);

    @Query("""
        select coalesce(avg(f.revenue), 0) from SmeMonthlyFinancials f
        where f.sme.smeId = :smeId and f.periodMonth between :fromMonth and :toMonth
    """)
    BigDecimal avgRevenue(UUID smeId, LocalDate fromMonth, LocalDate toMonth);

    @Query("""
        select coalesce(avg(f.operatingCashFlow), 0) from SmeMonthlyFinancials f
        where f.sme.smeId = :smeId and f.periodMonth between :fromMonth and :toMonth
    """)
    BigDecimal avgOCF(UUID smeId, LocalDate fromMonth, LocalDate toMonth);

}
