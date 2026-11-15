package org.sportsclub.dam.sports_club_manager.infrastructure.budget;
import org.sportsclub.dam.sports_club_manager.domain.model.Season;
import org.sportsclub.dam.sports_club_manager.domain.model.SeasonBudget;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Profile;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@Repository
@Profile("in-memory")
public class InMemorySeasonBudgetStore implements SeasonBudgetStore {
    private final Map<UUID, SeasonBudget> budgets = new ConcurrentHashMap<>();
    @Override public SeasonBudget save(SeasonBudget budget) { budgets.put(budget.getId(), budget); return budget; }
    @Override public Optional<SeasonBudget> findById(UUID id) { return Optional.ofNullable(budgets.get(id)); }
    @Override public Optional<SeasonBudget> findBySeason(Season season) {
        return budgets.values().stream().filter(b -> b.getSeason().equals(season)).findFirst();
    }
    @Override public List<SeasonBudget> findAll() { return List.copyOf(budgets.values()); }
}
