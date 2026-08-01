package com.arete.korbly.modules.sme.service;

import com.arete.korbly.modules.shared.exceptions.SMENotFound;
import com.arete.korbly.modules.sme.domain.SME;
import com.arete.korbly.modules.sme.domain.SmeMonthlyFinancials;
import com.arete.korbly.modules.sme.dto.SfmBulkUpsertDTO;
import com.arete.korbly.modules.sme.dto.SfmSeriesPoint;
import com.arete.korbly.modules.sme.dto.SfmSeriesResponse;
import com.arete.korbly.modules.sme.dto.SfmUpsertDTO;
import com.arete.korbly.modules.sme.persistence.SMERepository;
import com.arete.korbly.modules.sme.persistence.SmeFinancialsRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class SmeFinancialMonthlyService {
    private final SmeFinancialsRepository repo;
    private final SMERepository smeRepo;

    public SmeFinancialMonthlyService(SmeFinancialsRepository repo, SMERepository smeRepo) {
        this.repo = repo;
        this.smeRepo = smeRepo;
    }

    @CacheEvict(value = "dashboard", key = "#smeId")
    public SmeMonthlyFinancials upsert(SfmUpsertDTO dto, UUID smeId) {
        SME sme = smeRepo.findSMEBySmeId(smeId)
                .orElseThrow(() -> new SMENotFound("SME not found"));

        LocalDate normalized = dto.month().withDayOfMonth(1);
        validateAmounts(dto.revenue(), dto.operatingCashflow());

        Optional<SmeMonthlyFinancials> existing = repo.findOne(smeId, normalized);
        SmeMonthlyFinancials row = existing.orElseGet(() -> {
            SmeMonthlyFinancials nf = new SmeMonthlyFinancials();
            nf.setSme(sme);
            nf.setPeriodMonth(normalized);
            return nf;
        });

        row.setRevenue(nz(dto.revenue()));
        row.setOperatingCashFlow(nz(dto.operatingCashflow()));
        return repo.save(row);
    }

    @CacheEvict(value = "dashboard", key = "#smeId")
    @Transactional
    public List<SmeMonthlyFinancials> bulkUpsert(SfmBulkUpsertDTO bulk, UUID smeId) {
        SME sme = smeRepo.findSMEBySmeId(smeId)
                .orElseThrow(() -> new SMENotFound("SME not found"));

        List<SmeMonthlyFinancials> out = new ArrayList<>();
        for (SfmUpsertDTO r : bulk.rows()) {
            LocalDate normalized = r.month().withDayOfMonth(1);
            validateAmounts(r.revenue(), r.operatingCashflow());

            Optional<SmeMonthlyFinancials> existing = repo.findOne(smeId, normalized);
            SmeMonthlyFinancials row = existing.orElseGet(() -> {
                SmeMonthlyFinancials nf = new SmeMonthlyFinancials();
                nf.setSme(sme);
                nf.setPeriodMonth(normalized);
                return nf;
            });
            row.setRevenue(nz(r.revenue()));
            row.setOperatingCashFlow(nz(r.operatingCashflow()));
            out.add(row);
        }
        return repo.saveAll(out);
    }

    @Transactional(readOnly = true)
    public SfmSeriesResponse series(UUID smeId, Integer monthsBack) {
        int window = (monthsBack == null || monthsBack < 1) ? 12 : monthsBack;
        LocalDate end = java.time.LocalDate.now().withDayOfMonth(1);
        LocalDate start = end.minusMonths(window - 1);

        List<SmeMonthlyFinancials> rows = repo.findRange(smeId, start, end);
        List<SfmSeriesPoint> points = rows.stream()
                .map(r -> new SfmSeriesPoint(
                        r.getPeriodMonth().toString().substring(0, 7), // "YYYY-MM"
                        nz(r.getRevenue()),
                        nz(r.getOperatingCashFlow())
                ))
                .toList();

        BigDecimal avgRev = repo.avgRevenue(smeId, start, end);
        BigDecimal avgOCF = repo.avgOCF(smeId, start, end);

        return new SfmSeriesResponse(smeId, Timestamp.from(Instant.now()), points, avgRev, avgOCF);
    }

    private void validateAmounts(BigDecimal rev, BigDecimal ocf) {
        // adjust if you allow negatives for ocf
        if (rev != null && rev.signum() < 0) {
            throw new IllegalArgumentException("Revenue cannot be negative");
        }
    }

    private java.math.BigDecimal nz(java.math.BigDecimal v) {
        return v == null ? java.math.BigDecimal.ZERO : v;
    }
}
