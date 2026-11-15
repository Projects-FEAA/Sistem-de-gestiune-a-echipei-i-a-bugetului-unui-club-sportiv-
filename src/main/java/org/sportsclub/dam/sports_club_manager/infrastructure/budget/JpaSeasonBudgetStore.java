package org.sportsclub.dam.sports_club_manager.infrastructure.budget;

import org.sportsclub.dam.sports_club_manager.domain.model.Season;
import org.sportsclub.dam.sports_club_manager.domain.model.SeasonBudget;
import org.sportsclub.dam.sports_club_manager.infrastructure.persistence.PersistenceMapper;
import org.sportsclub.dam.sports_club_manager.infrastructure.persistence.repository.PlayerJpaRepository;
import org.sportsclub.dam.sports_club_manager.infrastructure.persistence.repository.SeasonBudgetJpaRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Primary
public class JpaSeasonBudgetStore implements SeasonBudgetStore {
    private final SeasonBudgetJpaRepository repository;
    private final PlayerJpaRepository playerRepository;
    private final PersistenceMapper mapper;

    public JpaSeasonBudgetStore(SeasonBudgetJpaRepository repository,
                                PlayerJpaRepository playerRepository,
                                PersistenceMapper mapper) {
        this.repository = repository;
        this.playerRepository = playerRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public SeasonBudget save(SeasonBudget budget) {
        var entity = mapper.toJpa(budget, playerRepository::getReferenceById);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SeasonBudget> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SeasonBudget> findBySeason(Season season) {
        return repository.findBySeason_StartYearAndSeason_EndYear(
                season.startYear(), season.endYear()).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeasonBudget> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }
}
