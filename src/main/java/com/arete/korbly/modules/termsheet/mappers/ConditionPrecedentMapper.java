package com.arete.korbly.modules.termsheet.mappers;

import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.termsheet.domain.ConditionsPrecedent;
import com.arete.korbly.modules.termsheet.dto.CPRequest;
import com.arete.korbly.modules.termsheet.dto.CPResponse;
import com.arete.korbly.modules.termsheet.dto.CPUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ConditionPrecedentMapper {
    @Mapping(target = "sheet", ignore = true)      //to be set in service
    @Mapping(target = "approvedBy", ignore = true) //handled later
    ConditionsPrecedent toEntity(CPRequest request);

    void updateEntityFromDto(CPUpdateRequest dto, @MappingTarget ConditionsPrecedent entity);

    @Mapping(target = "sheetId", source = "sheet.termSheetId")
    @Mapping(target = "approvedBy", source = "approvedBy.userId")
    CPResponse toResponse(ConditionsPrecedent entity);

    default AppUser map(UUID value){
        if(value == null) return null;
        return AppUser.builder().userId(value).build();
    }

    List<CPResponse> toResponseList(List<ConditionsPrecedent> entities);
}
