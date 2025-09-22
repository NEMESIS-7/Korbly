package com.arete.korbly.modules.termsheet.persistence;

import com.arete.korbly.modules.termsheet.domain.TermSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TermSheetRepository extends JpaRepository<TermSheet, UUID> {

    @Query("select t from TermSheet t where t.parent.termSheetId = :parentId order by t.sheetVersion")
    List<TermSheet> findTermSheet(UUID parentId);
}
