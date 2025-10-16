package com.arete.korbly.modules.credit.mapper;

import com.arete.korbly.modules.credit.domain.CreditMemo;
import com.arete.korbly.modules.credit.dto.CreditMemoDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreditDTOMapper {

    CreditMemo creditMemoDTOToCreditMemoEntity(CreditMemoDTO creditMemoDTO);

    CreditMemoDTO creditMemoEntityToCreditMemoDTO(CreditMemo creditMemo);

}
