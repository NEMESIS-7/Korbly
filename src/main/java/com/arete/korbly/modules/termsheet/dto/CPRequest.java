package com.arete.korbly.modules.termsheet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CPRequest {
    private UUID sheetId;
    private String title;
    private String description;
    private Boolean required;
}
