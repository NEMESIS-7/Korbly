package com.arete.korbly.modules.credit.persistence;

import com.arete.korbly.modules.credit.domain.CreditMemo;
import com.arete.korbly.modules.sme.dto.CreditHealthDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CreditMemoRepository extends JpaRepository<CreditMemo, UUID> {

    @Query("select c from CreditMemo c where c.sme.smeId = :smeId and c.deleteYn = 'N' order by c.evaluatedAt DESC")
    List<CreditMemo> findSmeCreditScores(UUID smeId);

    @Query("select c from CreditMemo c where c.creditMemoId = :creditMemoId and c.deleteYn = 'N'")
    Optional<CreditMemo> findCreditMemoById(UUID creditMemoId);

    @Transactional
    @Modifying
    @Query("update CreditMemo c set c.deleteYn = 'Y' where c.creditMemoId = :creditMemoId")
    void deleteCreditMemo(UUID creditMemoId);

    @Transactional
    @Modifying
    @Query("update CreditMemo c set c.deleteYn = 'Y' where c.sme.smeId = :smeId and c.deleteYn = 'N'")
    void deleteSMECreditMemos(UUID smeId);

    @Query("select c from CreditMemo c where c.deleteYn = 'N'")
    List<CreditMemo> getAllCreditMemo();

    @Query("""
                select new com.arete.korbly.modules.sme.dto.CreditHealthDTO(
                    cm.dscr,
                    cm.icr,
                    cm.altmanScore,
                    cm.ohlsonScore,
                    ''
                )
                from CreditMemo cm
                where cm.sme.smeId = :smeId
                  and cm.isLatest = true
            """)
    CreditHealthDTO latestHealth(UUID smeId);

    /*    @Query("""
        select new com.arete.korbly.modules.sme.dto.CreditHealthDTO(
            cm.dscr,
            cm.icr,
            cm.leverage,
            cm.altmanScore,
            cm.ohlsonScore
        )
        from CreditMemo cm
        where cm.sme.smeId = :smeId
          and cm.isLatest = true
    """)
    CreditHealthDTO latestHealth(UUID smeId);*/

}
