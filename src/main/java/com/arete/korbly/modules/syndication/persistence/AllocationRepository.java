package com.arete.korbly.modules.syndication.persistence;

import com.arete.korbly.modules.syndication.domain.Allocation;
import com.arete.korbly.modules.syndication.domain.Tranche;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AllocationRepository extends JpaRepository<Allocation, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Tranche t where t.trancheId = :trancheId")
    Optional<Tranche> findByTrancheIdForUpdate(UUID trancheId);

    @Query("select case when count(a) > 0 then true else false end " + "from Allocation  a where a.trancheId.trancheId = :trancheId")
    Boolean existsByTrancheId(UUID trancheId);

    @Query("select a from Allocation a where a.trancheId.trancheId = :trancheId")
    List<Allocation> findAllocationByTrancheId(UUID trancheId);

    @Query("select a from Allocation a where a.allocationId = :allocationId")
    Optional<Allocation> findById(UUID allocationId);

    @Query("select a from Allocation a where a.deleteYn = 'N'")
    Page<Allocation> getAllAllocation(Pageable pageable);

    @Query("select a from Allocation a  where a.trancheId.trancheId = :trancheId")
    Page<Allocation> findAllocationsByTrancheId(UUID trancheId, Pageable pageable);

    @Query("select a from Allocation a where a.investorId.investorId = :investorId")
    Page<Allocation> findAllocationsByInvestorId(UUID investorId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(a.amount), 0) FROM Allocation a WHERE a.trancheId.trancheId = :trancheId")
    Optional<BigDecimal> sumAllocatedAmountByTrancheId(UUID trancheId);



}
