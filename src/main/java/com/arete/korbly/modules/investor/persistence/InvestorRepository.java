package com.arete.korbly.modules.investor.persistence;

import com.arete.korbly.modules.investor.domain.Investor;
import com.arete.korbly.modules.investor.dto.InvestorPositionDTO;
import com.arete.korbly.modules.investor.dto.PortfolioSummaryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, UUID> {

    @Query("""
            select new com.arete.korbly.modules.investor.dto.PortfolioSummaryDTO(
                a.investorId.investorId,
                CURRENT_TIMESTAMP,
                COALESCE(SUM(a.amount), 0),
                CAST(COUNT(DISTINCT a.trancheId.trancheId) AS int),
                SUM(a.amount),
                SUM(a.amount * CAST(t.interestRate AS BigDecimal)),
                CAST(NULL AS BigDecimal),
                CAST(NULL AS BigDecimal),
                'simple_principal_sum',
                'coupon_weighted',
                'weighted_tranche_risk_v1'
            )
            from Allocation a
            join a.trancheId t
            where a.investorId.investorId = :investorId
              and a.allocationStatus in ('CONFIRMED','PENDING')
            group by a.investorId.investorId
            """)
    PortfolioSummaryDTO getPortfolioSummary(@Param("investorId") UUID investorId);


    @Query("""
                    select new com.arete.korbly.modules.investor.dto.InvestorPositionDTO(
                        t.trancheId,
                        t.trancheType,
                        a.amount,
                        t.amount,
                        t.interestRate,
                        a.amount
                    )
                    from Allocation a
                    join a.trancheId t
                    where a.investorId.investorId = :investorId
                      and a.allocationStatus in ('CONFIRMED','PENDING')
            """)
    List<InvestorPositionDTO> positionsSimple(UUID investorId);

    Optional<Investor> findByAppUserUserId(UUID appUserUserId);


 /*   @Query("""
                select new com.arete.korbly.modules.investor.dto.InvestorPositionDTO(
                    t.id,
                    a.amount,
                    (a.amount / NULLIF(t.amount,0)),
                    t.interestRate,
                    (
                      select vr.internalRateOfReturnAnnual
                        from ValuationResult vr
                       where vr.assumption.tranche = t
                         and vr.computedAt = (
                             select max(vr2.computedAt)
                               from ValuationResult vr2
                              where vr2.assumption.tranche = t
                         )
                    ),
                    (
                      select vr.netPresentValue
                        from ValuationResult vr
                       where vr.assumption.tranche = t
                         and vr.computedAt = (
                             select max(vr2.computedAt)
                               from ValuationResult vr2
                              where vr2.assumption.tranche = t
                         )
                    ),
                    COALESCE( (a.amount / NULLIF(t.amount,0)) *
                              COALESCE( (
                                select vr.netPresentValue
                                  from ValuationResult vr
                                 where vr.valuationAssumptionId = t
                                   and vr.computedAt = (
                                       select max(vr2.computedAt)
                                         from ValuationResult vr2
                                        where vr2.assumption.tranche = t
                                   )
                              ), t.amount ), a.amount )
                )
                from Allocation a
                join a.trancheId t
                where a.investorId.investorId = :investorId
                  and a.allocationStatus in ('ALLOCATED','FUNDED')
            """)
    List<InvestorPositionDTO> positionsMtm(UUID investorId);

    @Query("""
                select new com.arete.korbly.modules.investor.dto.PortfolioSummaryDTO(
                    :investorId,
                    CURRENT_TIMESTAMP,
                    COALESCE(SUM(a.amount), 0),
                    COUNT(DISTINCT a.trancheId.trancheId),
                    SUM(a.amount),
                    SUM(a.amount * COALESCE((
                        select vr.internalRateOfReturnAnnual
                          from ValuationResult vr
                         where vr.valuationAssumptionId = t.trancheId
                           and vr.computedAt = (
                               select max(vr2.computedAt)
                                 from ValuationResult vr2
                                where vr2.valuationAssumptionId = t.trancheId
                           )
                    ), t.interestRate)),
                    SUM(a.amount * COALESCE((
                        select vr.netPresentValue
                          from ValuationResult vr
                         where vr.valuationAssumptionId = t.trancheId
                           and vr.computedAt = (
                               select max(vr2.computedAt)
                                 from ValuationResult vr2
                                where vr2.valuationAssumptionId = t.trancheId
                           )
                    ), t.amount)),
                    SUM(a.amount * COALESCE((
                        select cm.riskComposite
                          from CreditMemo cm
                         where cm.sme = t.deal.smeInvolved
                           and (cm.isLatest = true or cm.isLatest is null)
                    ), 0)),
                    'mark_to_model',
                    'irr_weighted',
                    'weighted_tranche_risk_v1'
                )
                from Allocation a
                join a.trancheId t
                where a.investorId.investorId = :investorId
                  and a.allocationStatus in ('CONFIRMED','PENDING')
            """)
    PortfolioSummaryDTO summaryMtm(UUID investorId);*/
}
