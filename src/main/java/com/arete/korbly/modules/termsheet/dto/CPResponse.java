package com.arete.korbly.modules.termsheet.dto;

import com.arete.korbly.modules.shared.domain.AppUser;
import com.arete.korbly.modules.termsheet.enums.CPCode;
import com.arete.korbly.modules.termsheet.enums.CPStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CPResponse {
    private UUID cpId;
    private UUID sheetId;
    private CPCode code;

    private String title;
    private String description;
    private Boolean required;

    private CPStatus status;
    private String evidenceFileKey;
    private String note;
    private String waiverReason;

    private AppUser approvedBy;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
