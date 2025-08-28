package com.arete.korbly.modules.shared.persistence;

import com.arete.korbly.modules.shared.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

    Optional<AppUser> findByPrimaryContactEmail(String primaryContactEmail);

    @Query("select a from AppUser a where a.userId = :appUserId")
    Optional<AppUser> findAppUserById(UUID appUserId);
}
