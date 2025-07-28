package com.arete.korbly.modules.sme.mapper;

import com.arete.korbly.modules.sme.domain.SME;
import com.arete.korbly.modules.sme.dto.SMEDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SMEMapper {

    SMEDTO smeEntityToSMEDto(SME sme);

    SME smeDTOToSMEEntity(SMEDTO smedto);
}
