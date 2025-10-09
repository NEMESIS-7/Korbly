package com.arete.korbly.modules.termsheet.util;

import com.arete.korbly.modules.shared.enums.CpMeta;
import com.arete.korbly.modules.shared.enums.DeleteYn;
import com.arete.korbly.modules.termsheet.domain.ConditionsPrecedent;
import com.arete.korbly.modules.termsheet.domain.TermSheet;
import com.arete.korbly.modules.termsheet.enums.CPCode;
import com.arete.korbly.modules.termsheet.enums.CPStatus;
import com.arete.korbly.modules.termsheet.persistence.ConditionsPrecedentRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class DefaultCpGenerator {

    private final ConditionsPrecedentRepository repo;

    public DefaultCpGenerator(ConditionsPrecedentRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public List<ConditionsPrecedent> generateDefaults(TermSheet sheet) {
        Set<CPCode> codes = new LinkedHashSet<>(List.of(
                CPCode.CORP_BOARD_RESOLUTION_BORROW,
                CPCode.INS_LOSS_PAYEE_ENDORSEMENT,
                CPCode.SEC_PERFECTION_FILINGS_COMPLETE,
                CPCode.KYC_BORROWER_COMPLETE,
                CPCode.FIN_TAX_CLEARANCE_CERT
        ));


        List<ConditionsPrecedent> toSave = new ArrayList<>();
        for (CPCode code : codes) {
            if (repo.existsBySheetAndCodeAndDeleteYn(sheet, code, DeleteYn.N)) {
                continue;
            }

             CpMeta meta = CpCatalog.META.get(code);
            if (meta == null){
                continue;
            }

            ConditionsPrecedent cp = new ConditionsPrecedent();
            cp.setSheet(sheet);
            cp.setCode(code);
            cp.setTitle(meta.title());
            cp.setDescription(meta.description());
            cp.setRequired(meta.required());
            cp.setStatus(CPStatus.PENDING);
            cp.setEvidenceFileKey(null);
            cp.setNote(null);
            toSave.add(cp);
        }

        try {
            return repo.saveAll(toSave);
        } catch (DataIntegrityViolationException e) {
            return repo.findBySheetAndDeleteYn(sheet, DeleteYn.N);
        }
    }
}