package com.arete.korbly.modules.shared.Mappers;

import com.arete.korbly.modules.investor.domain.Investor;
import com.arete.korbly.modules.shared.dto.InvestorDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvestorMapper {


    Investor investorDTOToEntity(InvestorDTO investorDTO);

    InvestorDTO investorEntityToInvestorDTO(Investor investor);

}
