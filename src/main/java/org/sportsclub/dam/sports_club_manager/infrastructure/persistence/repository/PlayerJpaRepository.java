package org.sportsclub.dam.sports_club_manager.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sportsclub.dam.sports_club_manager.infrastructure.persistence.entity.PlayerJpaEntity;

import java.util.UUID;

public interface PlayerJpaRepository extends JpaRepository<PlayerJpaEntity, UUID> {
    boolean existsByJerseyNumber(Integer jerseyNumber);
    boolean existsByJerseyNumberAndIdNot(Integer jerseyNumber, UUID excludedPlayerId);
}
