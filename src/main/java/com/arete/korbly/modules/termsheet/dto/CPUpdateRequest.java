package com.arete.korbly.modules.termsheet.dto;

import com.arete.korbly.modules.termsheet.enums.CPStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CPUpdateRequest {
    private CPStatus status;
    private String note;
    private String waiverReason;
    private String evidenceFileKey;
}
