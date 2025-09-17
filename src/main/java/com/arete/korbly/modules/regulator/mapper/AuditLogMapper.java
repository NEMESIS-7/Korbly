package com.arete.korbly.modules.regulator.mapper;

import com.arete.korbly.modules.regulator.domain.AuditLog;
import com.arete.korbly.modules.regulator.dto.AuditLogDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {

    AuditLogDTO entityToDTO(AuditLog auditLog);

    AuditLog dtoToEntity(AuditLogDTO auditLogDTO);
}
