package com.arete.korbly.modules.sme.service;

import com.arete.korbly.modules.credit.persistence.CreditMemoRepository;
import com.arete.korbly.modules.sme.dto.CreditHealthDTO;
import com.arete.korbly.modules.sme.dto.RevenuePoint;
import com.arete.korbly.modules.sme.dto.SmeDashboardDto;
import com.arete.korbly.modules.sme.persistence.SMERepository;
import com.arete.korbly.modules.sme.persistence.SmeFinancialsRepository;
import com.arete.korbly.modules.syndication.persistence.DealRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class SmeDashboardService {

    private final SmeFinancialsRepository financialRepo; // requires SmeFinancialMonthly table
    private final DealRepository dealRepo;
    private final CreditMemoRepository creditMemoRepo;
    private final SMERepository smeRepository;

    public SmeDashboardService(
            SmeFinancialsRepository financialRepo,
            DealRepository dealRepo,
            CreditMemoRepository creditMemoRepo,
            SMERepository smeRepository
    ) {
        this.financialRepo = financialRepo;
        this.dealRepo = dealRepo;
        this.creditMemoRepo = creditMemoRepo;
        this.smeRepository = smeRepository;
    }

    @Cacheable(value = "dashboard", key = "#smeId")
    public SmeDashboardDto getDashboard(UUID smeId, Integer rangeMonths) {
        int window = (rangeMonths == null || rangeMonths < 1) ? 12 : rangeMonths;
        LocalDate nowMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate fromMonth = nowMonth.minusMonths(window - 1);

        List<RevenuePoint> revenue = smeRepository != null
                ? smeRepository.revenueSeries(smeId, fromMonth)
                : List.of();

        BigDecimal avgOCF = smeRepository != null
                ? smeRepository.avgOperatingCashflow(smeId, fromMonth)
                : BigDecimal.ZERO;

        long openApps = smeRepository.countOpenApplications(smeId);

        CreditHealthDTO raw = creditMemoRepo.latestHealth(smeId);
        CreditHealthDTO health = withLabel(raw);

        return new SmeDashboardDto(
                smeId,
                Timestamp.from(Instant.now()),
                revenue,
                (int) openApps,
                avgOCF,
                health
        );
    }

    private CreditHealthDTO withLabel(CreditHealthDTO h) {
        if (h == null) return null;
        String label = classify(h);
        return new CreditHealthDTO(h.dscr(), h.icr(), h.altmanZ(), h.ohlsonO(), label);
    }


    private String classify(CreditHealthDTO h) {
        double dscr = nz(h.dscr());
        double icr = nz(h.icr());
        double z   = h.altmanZ().doubleValue();
        // Strong if DSCR ≥ 1.5, AltmanZ ≥ 2.6, ICR ≥ 3
        // Weak if DSCR < 1.1 or AltmanZ < 1.8; else Moderate
        boolean strong = dscr >= 1.5 && z >= 2.6 && icr >= 3.0;
        boolean weak   = dscr < 1.1  || z < 1.8;
        if (strong) return "Healthy";
        if (weak)   return "Weak";
        return "Moderate";
    }

    private double nz(Double d) { return d == null ? 0.0 : d; }
}