package org.sportsclub.dam.sports_club_manager.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionStatus;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionType;
import org.sportsclub.dam.sports_club_manager.infrastructure.persistence.entity.TransactionJpaEntity;

import java.util.List;
import java.util.UUID;

public interface TransactionJpaRepository extends JpaRepository<TransactionJpaEntity, UUID> {
    List<TransactionJpaEntity> findByBudget_Id(UUID budgetId);
    List<TransactionJpaEntity> findByBudget_IdAndTypeAndStatus(
            UUID budgetId, TransactionType type, TransactionStatus status);
}
