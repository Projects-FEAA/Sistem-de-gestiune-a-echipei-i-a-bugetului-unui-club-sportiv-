package org.sportsclub.dam.sports_club_manager.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sportsclub.dam.sports_club_manager.infrastructure.persistence.entity.SeasonBudgetJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface SeasonBudgetJpaRepository extends JpaRepository<SeasonBudgetJpaEntity, UUID> {
    Optional<SeasonBudgetJpaEntity> findBySeason_StartYearAndSeason_EndYear(int startYear, int endYear);
}
