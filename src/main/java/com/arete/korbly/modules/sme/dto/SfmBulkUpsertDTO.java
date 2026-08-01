package com.arete.korbly.modules.sme.dto;

import java.util.List;

public record SfmBulkUpsertDTO(
        List<SfmUpsertDTO> rows
) {}