package com.arete.korbly.modules.termsheet.persistence;

import com.arete.korbly.modules.termsheet.domain.TermSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TermSheetRepository extends JpaRepository<TermSheet, UUID> {

    @Query("select t from TermSheet t where t.parent.termSheetId = :parentId order by t.sheetVersion")
    List<TermSheet> findTermSheet(UUID parentId);

    @Query("select t from TermSheet t where t.parent.termSheetId = :parentId and t.isLatest = true and t.deleteYn = 'N'")
    Optional<TermSheet> findLatestTermsheet(UUID parentId);

    @Query("select t from TermSheet t where t.termSheetId = :sheetId and t.deleteYn = 'N'")
    Optional<TermSheet> getByTermSheetId (UUID sheetId);

    @Query("select t from TermSheet t where t.parent.termSheetId = :parentId and t.deleteYn = 'N'")
    List<TermSheet> getAllVersions(UUID parentId);

    @Query("select t from TermSheet t where t.parent.termSheetId = :parentId and t.isLatest = true and t.deleteYn = 'N'")
    Optional<TermSheet> getLatestVersion(UUID parentId);

    @Query("select t from TermSheet t where t.dealId.dealId = :dealId and t.deleteYn = 'N'")
    List<TermSheet> findByDealID(UUID dealId);

    @Query("select t from TermSheet t where t.trancheId.trancheId = :trancheId and t.deleteYn = 'N'")
    List<TermSheet> findByTrancheId(UUID trancheId);

    @Query("select t from TermSheet t where t.smeId.smeId = :smeId and t.deleteYn = 'N'")
    List<TermSheet> findBySmeId(UUID smeId);

    @Transactional
    @Modifying
    @Query("update TermSheet t set t.isLatest = false where t.parent.termSheetId = :parentId")
    void markAllAsNotLatest(UUID parentId);
}
