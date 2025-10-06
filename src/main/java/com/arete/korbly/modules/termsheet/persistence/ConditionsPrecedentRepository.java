package com.arete.korbly.modules.termsheet.persistence;

import com.arete.korbly.modules.shared.enums.DeleteYn;
import com.arete.korbly.modules.termsheet.domain.ConditionsPrecedent;
import com.arete.korbly.modules.termsheet.domain.TermSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConditionsPrecedentRepository extends JpaRepository<ConditionsPrecedent, UUID> {
    @Query("select cp from ConditionsPrecedent cp where cp.sheet.termSheetId = :termSheetId")
    List<ConditionsPrecedent> findBySheetId(UUID termSheetId);

    List<ConditionsPrecedent> findBySheetAndDeleteYn(TermSheet sheet, DeleteYn deleteYn);
}
