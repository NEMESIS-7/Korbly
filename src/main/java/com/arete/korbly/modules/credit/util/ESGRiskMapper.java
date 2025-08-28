package com.arete.korbly.modules.credit.util;

import com.arete.korbly.modules.credit.ESGRiskRating;
import com.arete.korbly.modules.shared.enums.SMEIndustry;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class ESGRiskMapper {

    private static final Map<SMEIndustry, ESGRiskRating> riskMap = new EnumMap<>(SMEIndustry.class);

    static {
        riskMap.put(SMEIndustry.AGRICULTURE_AGRIBUSINESS, ESGRiskRating.HIGH);
        riskMap.put(SMEIndustry.MANUFACTURING, ESGRiskRating.HIGH);
        riskMap.put(SMEIndustry.TECHNOLOGY_DIGITAL_SERVICES, ESGRiskRating.LOW_MODERATE);
        riskMap.put(SMEIndustry.HEALTHCARE, ESGRiskRating.MEDIUM);
        riskMap.put(SMEIndustry.EDUCATION, ESGRiskRating.LOW);
        riskMap.put(SMEIndustry.RENEWABLE_ENERGY, ESGRiskRating.MEDIUM);
        riskMap.put(SMEIndustry.CONSTRUCTION_REAL_ESTATE, ESGRiskRating.HIGH);
        riskMap.put(SMEIndustry.TRANSPORT_LOGISTICS, ESGRiskRating.HIGH);
        riskMap.put(SMEIndustry.RETAIL_TRADE, ESGRiskRating.MEDIUM);
        riskMap.put(SMEIndustry.FINANCIAL_SERVICES, ESGRiskRating.LOW);
    }

    public ESGRiskRating getRiskRating(SMEIndustry sector) {
        return riskMap.getOrDefault(sector, ESGRiskRating.UNKNOWN);
    }
}
