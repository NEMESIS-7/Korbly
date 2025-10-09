package com.arete.korbly.modules.regulator.mapper;

import com.arete.korbly.modules.regulator.domain.Regulator;
import com.arete.korbly.modules.regulator.dto.RegulatorDTO;
import com.arete.korbly.modules.shared.domain.AppUser;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface RegulatorMapper {

    RegulatorDTO entityToDTO(Regulator regulator);

    Regulator DTOtoEntity(RegulatorDTO regulatorDTO);


    default UUID mapAppUserId(AppUser appUser){
        return appUser != null ? appUser.getUserId() : null;
    }

    default AppUser mapAppUser(UUID appUser){
        if(appUser == null) return null;
        return AppUser.builder().userId(appUser).build();
    }
}
