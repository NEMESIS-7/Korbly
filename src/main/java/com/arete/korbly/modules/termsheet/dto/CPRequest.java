package com.arete.korbly.modules.termsheet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CPRequest {
    private String title;
    private String description;
    private Boolean required;
    private String note;
}
