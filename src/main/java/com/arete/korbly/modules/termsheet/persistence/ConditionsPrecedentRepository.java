package com.arete.korbly.modules.termsheet.persistence;

import com.arete.korbly.modules.termsheet.domain.ConditionsPrecedent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConditionsPrecedentRepository extends JpaRepository<ConditionsPrecedent, UUID> {

}
