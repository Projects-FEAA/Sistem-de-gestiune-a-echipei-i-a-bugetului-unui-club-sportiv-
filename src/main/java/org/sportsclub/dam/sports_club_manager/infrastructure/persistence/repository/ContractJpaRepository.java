package org.sportsclub.dam.sports_club_manager.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sportsclub.dam.sports_club_manager.domain.enums.ContractStatus;
import org.sportsclub.dam.sports_club_manager.infrastructure.persistence.entity.ContractJpaEntity;

import java.util.List;
import java.util.UUID;

public interface ContractJpaRepository extends JpaRepository<ContractJpaEntity, UUID> {
    List<ContractJpaEntity> findByPlayer_Id(UUID playerId);
    List<ContractJpaEntity> findByPlayer_IdAndStatus(UUID playerId, ContractStatus status);
}
