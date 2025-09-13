package com.arete.korbly.modules.regulator.service;

import com.arete.korbly.modules.regulator.dto.AuditLogDTO;
import com.arete.korbly.modules.regulator.dto.CreateRegulatorDTO;
import com.arete.korbly.modules.regulator.dto.RegulatorDTO;
import com.arete.korbly.modules.regulator.dto.RegulatorDealViewDTO;
import com.arete.korbly.modules.regulator.enums.RegulatorStatus;
import com.arete.korbly.modules.syndication.enums.DealStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IRegulatorService {
    /**
     *
     * @param dto
     * @param adminId
     * @return
     */
    RegulatorDTO createRegulator(CreateRegulatorDTO dto, UUID adminId);


    /**
     *
     * @param pageable
     * @return
     */
    Page<RegulatorDTO> listAllRegulators(Pageable pageable);

    /**
     *
     * @param regulatorId
     * @param regulatorStatus
     * @return
     */
    RegulatorDTO updateRegulatorStatus(UUID regulatorId, RegulatorStatus regulatorStatus);

    /**
     *
     * @param regulatorId
     * @param pageable
     * @return
     */
    Page<RegulatorDealViewDTO> getDealsForRegulator(UUID regulatorId, Pageable pageable);

    /**
     *
     * @param regulatorId
     * @param dealId
     * @return
     */
    RegulatorDealViewDTO getDealDetailForRegulator(UUID regulatorId, UUID dealId);

    /**
     *
     * @param regulatorId
     * @param entityType
     * @param entityId
     * @param pageable
     * @return
     */
    List<AuditLogDTO> getAuditLogsForEntity(UUID regulatorId, String entityType, UUID entityId, Pageable pageable);

    /**
     *
     * @param regulatorId
     * @param pageable
     * @return
     */
    List<AuditLogDTO> getAllAuditLogs(UUID regulatorId, Pageable pageable);

    /**
     *
     * @param regulatorId
     * @param sector
     * @param status
     * @param pageable
     * @return
     */
    List<RegulatorDealViewDTO> searchDeals(UUID regulatorId, String sector, DealStatus status, Pageable pageable);

}