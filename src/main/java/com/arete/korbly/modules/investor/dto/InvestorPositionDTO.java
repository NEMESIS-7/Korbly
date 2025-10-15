package com.arete.korbly.modules.investor.dto;

import com.arete.korbly.modules.syndication.enums.TrancheType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Getter
@Setter
public class InvestorPositionDTO {
    private final UUID trancheId;
    private final TrancheType trancheType;
    private final BigDecimal investorAmount;
    private final BigDecimal trancheAmount;
    private final BigDecimal investorShare;
    private final BigDecimal couponAnnual;
    private final BigDecimal trancheIRR;
    private final BigDecimal trancheNPV;
    private final BigDecimal positionValue;

   /* public InvestorPositionDTO(
            UUID trancheId,
            BigDecimal investorAmount,
            BigDecimal trancheAmount,
            BigDecimal couponAnnual,
            BigDecimal trancheIRR,
            BigDecimal trancheNPV,
            BigDecimal positionValue
    ) {
        this.trancheId = trancheId;
        this.investorAmount = investorAmount;
        this.trancheAmount = trancheAmount;
        this.couponAnnual = couponAnnual;
        this.trancheIRR = trancheIRR;
        this.trancheNPV = trancheNPV;
        this.positionValue = positionValue;

        if (trancheAmount == null || trancheAmount.compareTo(BigDecimal.ZERO) == 0) {
            this.investorShare = BigDecimal.ZERO;
        } else {
            this.investorShare = investorAmount.divide(trancheAmount, 6, RoundingMode.HALF_UP);
        }
    }*/

    public InvestorPositionDTO(UUID trancheId,
                               TrancheType trancheType,
                               BigDecimal investorAmount,
                               BigDecimal trancheAmount,
                               BigDecimal couponAnnual,
                               BigDecimal positionValue) {
        this.trancheId = trancheId;
        this.trancheType = trancheType;
        this.investorAmount = investorAmount;
        this.trancheAmount = trancheAmount;
        this.couponAnnual = couponAnnual;
        this.trancheIRR = null;
        this.trancheNPV = null;
        this.positionValue = positionValue;

        if (trancheAmount == null || trancheAmount.compareTo(BigDecimal.ZERO) == 0) {
            this.investorShare = BigDecimal.ZERO;
        } else {
            this.investorShare = investorAmount.divide(trancheAmount, 6, RoundingMode.HALF_UP);
        }
    }

    public InvestorPositionDTO(UUID trancheId,
                               TrancheType trancheType,
                               BigDecimal investorAmount,
                               BigDecimal trancheAmount,
                               Double couponAnnual,
                               BigDecimal positionValue) {
        this.trancheId = trancheId;
        this.trancheType = trancheType;
        this.investorAmount = investorAmount;
        this.trancheAmount = trancheAmount;
        this.couponAnnual = couponAnnual != null ? BigDecimal.valueOf(couponAnnual) : BigDecimal.ZERO;  // Convert to BigDecimal
        this.trancheIRR = null;
        this.trancheNPV = null;
        this.positionValue = positionValue;

        if (trancheAmount == null || trancheAmount.compareTo(BigDecimal.ZERO) == 0) {
            this.investorShare = BigDecimal.ZERO;
        } else {
            this.investorShare = investorAmount.divide(trancheAmount, 6, RoundingMode.HALF_UP);
        }
    }
}