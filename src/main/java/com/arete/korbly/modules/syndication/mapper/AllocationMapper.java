package com.arete.korbly.modules.syndication.mapper;

import com.arete.korbly.modules.syndication.domain.Allocation;
import com.arete.korbly.modules.syndication.dto.AllocationDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AllocationMapper {
    AllocationDTO mapEntityToDTO(Allocation allocation);

    Allocation mapDtoToEntity(AllocationDTO allocationDTO);
}
