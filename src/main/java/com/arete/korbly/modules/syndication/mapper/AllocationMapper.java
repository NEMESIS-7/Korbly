package com.arete.korbly.modules.syndication.mapper;

import com.arete.korbly.modules.investor.domain.Investor;
import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.syndication.domain.Allocation;
import com.arete.korbly.modules.syndication.domain.Tranche;
import com.arete.korbly.modules.syndication.dto.AllocationDTO;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AllocationMapper {
    AllocationDTO mapEntityToDTO(Allocation allocation);

    Allocation mapDtoToEntity(AllocationDTO allocationDTO);

    default UUID mapTrancheId(Tranche tranche){
        return tranche != null ? tranche.getTrancheId() : null;
    }

    default UUID mapInvestorId(Investor investor){
        return investor != null ? investor.getInvestorId() : null;
    }

    default UUID mapAppUserId(AppUser appUser){
        return appUser != null ? appUser.getUserId() : null;

    }

    default Tranche mapTranche(UUID tranche){
        if(tranche == null) return null;
        return Tranche.builder().trancheId(tranche).build();
    }

    default Investor mapInvestor(UUID investor){
        if(investor == null) return null;
        return Investor.builder().investorId(investor).build();
    }

    default AppUser mapAppUser(UUID appUser){
        if(appUser == null) return null;
        return AppUser.builder().userId(appUser).build();
    }
}
