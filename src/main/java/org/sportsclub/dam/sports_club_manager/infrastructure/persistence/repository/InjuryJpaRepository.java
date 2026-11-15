package org.sportsclub.dam.sports_club_manager.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sportsclub.dam.sports_club_manager.domain.enums.InjuryStatus;
import org.sportsclub.dam.sports_club_manager.infrastructure.persistence.entity.InjuryJpaEntity;

import java.util.List;
import java.util.UUID;

public interface InjuryJpaRepository extends JpaRepository<InjuryJpaEntity, UUID> {
    List<InjuryJpaEntity> findByPlayer_Id(UUID playerId);
    List<InjuryJpaEntity> findByPlayer_IdAndStatus(UUID playerId, InjuryStatus status);
}
