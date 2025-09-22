package com.arete.korbly.modules.termsheet.dto;

import com.arete.korbly.modules.termsheet.enums.TermSheetStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TermSheetUpdateRequest {
    private TermSheetStatus sheetStatus;
    private Timestamp signedAt;
}
