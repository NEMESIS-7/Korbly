package com.arete.korbly.modules.valuation.service;

import com.arete.korbly.modules.valuation.domain.CashFlowAssumption;
import com.arete.korbly.modules.valuation.dto.CashflowRow;

import java.util.List;

public interface ICashflowEngine {
    List<CashflowRow> buildSchedule(CashFlowAssumption a);
}
