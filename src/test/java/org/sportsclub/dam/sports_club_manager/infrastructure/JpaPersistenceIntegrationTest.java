package org.sportsclub.dam.sports_club_manager.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.sportsclub.dam.sports_club_manager.domain.enums.*;
import org.sportsclub.dam.sports_club_manager.domain.model.*;
import org.sportsclub.dam.sports_club_manager.infrastructure.budget.SeasonBudgetStore;
import org.sportsclub.dam.sports_club_manager.infrastructure.player.PlayerStore;
import org.sportsclub.dam.sports_club_manager.infrastructure.persistence.repository.PlayerJpaRepository;
import org.sportsclub.dam.sports_club_manager.infrastructure.persistence.repository.TransactionJpaRepository;
import org.sportsclub.dam.sports_club_manager.infrastructure.user.UserStore;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JpaPersistenceIntegrationTest {
    @Autowired private PlayerStore players;
    @Autowired private SeasonBudgetStore budgets;
    @Autowired private UserStore users;
    @Autowired private TransactionJpaRepository transactionRepository;
    @Autowired private PlayerJpaRepository playerRepository;

    @Test
    void playerAggregateAndChildrenArePersistedAndReconstructed() {
        Player player = new Player("Persistence Player", LocalDate.of(2001, 5, 10),
                "Defender", randomJerseyNumber());
        Contract contract = new Contract(player.getId(), ContractType.PERMANENT, null,
                new BigDecimal("1200"), BigDecimal.ZERO, LocalDate.of(2026, 7, 1), null);
        player.addContract(contract);
        Injury injury = new Injury(player.getId(), "Ankle", LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 4, 1));
        player.addInjury(injury);

        players.save(player);
        Player loaded = players.findById(player.getId()).orElseThrow();

        assertEquals(player.getId(), loaded.getId());
        assertEquals(1, loaded.getContracts().size());
        assertEquals(contract.getId(), loaded.getContracts().getFirst().getId());
        assertEquals(1, loaded.getInjuries().size());
        assertEquals(PlayerStatus.UNAVAILABLE, loaded.getStatus());
        var persistedPlayer = playerRepository.findById(player.getId()).orElseThrow();
        assertNotNull(persistedPlayer.getCreatedAt());
        assertNotNull(persistedPlayer.getUpdatedAt());

        loaded.updateDetails("Updated Persistence Player", loaded.getBirthDate(),
                loaded.getPosition(), loaded.getJerseyNumber());
        players.save(loaded);
        var updatedPlayer = playerRepository.findById(player.getId()).orElseThrow();
        assertEquals("Updated Persistence Player", updatedPlayer.getName());
        assertTrue(updatedPlayer.getUpdatedAt().compareTo(updatedPlayer.getCreatedAt()) >= 0);
    }

    @Test
    void budgetPersistsIncomeExpenseAndPlayerExpenseHierarchy() {
        Player player = new Player("Salary Player", LocalDate.of(2000, 2, 2),
                "Forward", randomJerseyNumber());
        players.save(player);

        Season season = new Season(2040, 2041);
        SeasonBudget budget = new SeasonBudget(season, new BigDecimal("5000"));
        budget.addTransaction(new IncomeTransaction(budget.getId(), IncomeCategory.SPONSORSHIPS,
                new BigDecimal("1000"), LocalDate.now(), "Sponsor"));
        budget.addTransaction(new ExpenseTransaction(budget.getId(), ExpenseCategory.TRAVEL,
                new BigDecimal("250"), LocalDate.now(), "Travel"));
        PlayerExpenseTransaction salary = new PlayerExpenseTransaction(budget.getId(), player.getId(),
                new BigDecimal("1200"), LocalDate.now(), "Player salary");
        budget.addTransaction(salary);

        budgets.save(budget);
        SeasonBudget loaded = budgets.findBySeason(season).orElseThrow();

        assertEquals(0, loaded.getIncome().compareTo(new BigDecimal("1000")));
        assertEquals(0, loaded.getSpent().compareTo(new BigDecimal("1450")));
        assertEquals(0, loaded.getRemaining().compareTo(new BigDecimal("4550")));
        assertTrue(loaded.getTransactions().stream().anyMatch(
                transaction -> transaction instanceof PlayerExpenseTransaction
                        && ((PlayerExpenseTransaction) transaction).getPlayerId().equals(player.getId())));
        assertEquals(3, transactionRepository.findByBudget_Id(loaded.getId()).size());
    }

    @Test
    void playerUserAssociationIsPersisted() {
        Player player = new Player("Account Player", LocalDate.of(2002, 6, 6),
                "Goalkeeper", randomJerseyNumber());
        players.save(player);
        User user = User.reconstitute(UUID.randomUUID(), "player-" + UUID.randomUUID(),
                "bcrypt-hash", UserRole.PLAYER, player.getId());

        users.save(user);
        User loaded = users.findByUsername(user.getUsername()).orElseThrow();

        assertEquals(user.getId(), loaded.getId());
        assertEquals(player.getId(), loaded.getPlayerId());
        assertEquals(UserRole.PLAYER, loaded.getRole());
    }

    private Integer randomJerseyNumber() {
        return 1 + (int) (Math.random() * 1_000_000);
    }
}
