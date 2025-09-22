package com.arete.korbly.modules.termsheet.mappers;

import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.termsheet.domain.ConditionsPrecedent;
import com.arete.korbly.modules.termsheet.domain.TermSheet;
import com.arete.korbly.modules.termsheet.dto.TermSheetDTO;
import com.arete.korbly.modules.termsheet.dto.TermSheetResponse;
import com.arete.korbly.modules.termsheet.enums.CPStatus;
import com.arete.korbly.modules.termsheet.enums.TermSheetStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TermSheetMapper {
    @Mapping(target = "dealId", ignore = true)     //to be set in service
    @Mapping(target = "trancheId", ignore = true)  //to be set in service
    @Mapping(target = "smeId", ignore = true)      //to be set in service
    @Mapping(target = "parent", ignore = true)   //handled in service if there's amendment
    @Mapping(target = "createdBy", ignore = true)  //set from security context
    TermSheet toEntity(TermSheetDTO request);


    default AppUser map(UUID value){
        if(value == null) return null;
        return AppUser.builder().userId(value).build();
    }
    default UUID map(TermSheet value){
        return value != null ? value.getTermSheetId() : null;
    }

    default TermSheet copyAndAmend(TermSheet existing, TermSheetDTO dto) {
        TermSheet amended = new TermSheet();

        amended.setDealId(existing.getDealId());
        amended.setTrancheId(existing.getTrancheId());
        amended.setSmeId(existing.getSmeId());
        amended.setParentId(existing.getParentId() != null ? existing.getParentId() : existing);

        amended.setCreatedBy(existing.getCreatedBy());
        amended.setCreatedAt(existing.getCreatedAt());
        amended.setUpdatedAt(Timestamp.from(Instant.now()));

        amended.setLoanAmount(dto.loanAmount() != null ? dto.loanAmount() : existing.getLoanAmount());
        amended.setInterestRate(dto.interestRate() != null ? dto.interestRate() : existing.getInterestRate());
        amended.setMaturityDate(dto.maturityDate() != null ? dto.maturityDate() : existing.getMaturityDate());
        amended.setAmortizationStructure(dto.amortizationStructure() != null ? dto.amortizationStructure() : existing.getAmortizationStructure());
        amended.setPrepaymentOption(dto.prepaymentOption() != null ? dto.prepaymentOption() : existing.getPrepaymentOption());
        amended.setOfferingPeriod(dto.offeringPeriod() != null ? dto.offeringPeriod() : existing.getOfferingPeriod());
        amended.setGuarantees(dto.guarantees() != null ? dto.guarantees() : existing.getGuarantees());
        amended.setCollateral(dto.collateral() != null ? dto.collateral() : existing.getCollateral());
        amended.setSeniority(dto.seniority() != null ? dto.seniority() : existing.getSeniority());
        amended.setCovenants(dto.covenants() != null ? dto.covenants() : existing.getCovenants());
        amended.setEventsOfDefault(dto.eventsOfDefault() != null ? dto.eventsOfDefault() : existing.getEventsOfDefault());
        amended.setDefaultRate(dto.defaultRate() != null ? dto.defaultRate() : existing.getDefaultRate());
        amended.setGracePeriods(dto.gracePeriods() != null ? dto.gracePeriods() : existing.getGracePeriods());
        amended.setGoverningLaw(dto.governingLaw() != null ? dto.governingLaw() : existing.getGoverningLaw());

        amended.setSheetStatus(TermSheetStatus.DRAFT);
        amended.setSignedAt(null);
        amended.setLatest(true);

        if (existing.getConditionsPrecedent() != null && !existing.getConditionsPrecedent().isEmpty()) {
            List<ConditionsPrecedent> copiedCPs = existing
                    .getConditionsPrecedent()
                    .stream()
                    .map(oldCp -> {
                        ConditionsPrecedent copy = new ConditionsPrecedent();
                        copy.setTitle(oldCp.getTitle());
                        copy.setDescription(oldCp.getDescription());
                        copy.setRequired(oldCp.getRequired());
                        copy.setStatus(CPStatus.PENDING);
                        copy.setNote(null);
                        copy.setEvidenceFileKey(null);
                        copy.setApprovedBy(null);
                        copy.setWaiverReason(null);
                        copy.setSheet(amended);
                        return copy;
                    })
                    .toList();
            amended.setConditionsPrecedent(copiedCPs);
        }

        return amended;
    }

    @Mapping(target = "dealId", source = "dealId.dealId")
    @Mapping(target = "trancheId", source = "trancheId.trancheId")
    @Mapping(target = "smeId", source = "smeId.smeId")
    @Mapping(target = "createdBy", source = "createdBy.userId")
    TermSheetResponse toResponse(TermSheet entity);

    List<TermSheetResponse> toResponseList(List<TermSheet> entities);
}
