package com.arete.korbly.modules.termsheet.mappers;

import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.termsheet.domain.TermSheet;
import com.arete.korbly.modules.termsheet.dto.TermSheetRequest;
import com.arete.korbly.modules.termsheet.dto.TermSheetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TermSheetMapper {
    @Mapping(target = "dealId", ignore = true)     //to be set in service
    @Mapping(target = "trancheId", ignore = true)  //to be set in service
    @Mapping(target = "smeId", ignore = true)      //to be set in service
    @Mapping(target = "parent", ignore = true)   //handled in service if there's amendment
    @Mapping(target = "createdBy", ignore = true)  //set from security context
    TermSheet toEntity(TermSheetRequest request);

    default AppUser map(UUID value){
        if(value == null) return null;
        return AppUser.builder().userId(value).build();
    }
    default UUID map(TermSheet value){
        return value != null ? value.getTermSheetId() : null;
    }

    @Mapping(target = "dealId", source = "dealId.dealId")
    @Mapping(target = "trancheId", source = "trancheId.trancheId")
    @Mapping(target = "smeId", source = "smeId.smeId")
    @Mapping(target = "createdBy", source = "createdBy.userId")
    TermSheetResponse toResponse(TermSheet entity);

    List<TermSheetResponse> toResponseList(List<TermSheet> entities);
}
