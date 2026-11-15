package org.sportsclub.dam.sports_club_manager.service;

import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;
import org.sportsclub.dam.sports_club_manager.domain.enums.*;
import org.sportsclub.dam.sports_club_manager.domain.exception.InsufficientBudgetException;
import org.sportsclub.dam.sports_club_manager.domain.factory.SeasonBudgetFactory;
import org.sportsclub.dam.sports_club_manager.domain.model.*;
import org.sportsclub.dam.sports_club_manager.service.audit.InMemoryAuditService;
import org.sportsclub.dam.sports_club_manager.service.validation.*;
import org.sportsclub.dam.sports_club_manager.service.workflow.*;
import org.sportsclub.dam.sports_club_manager.infrastructure.budget.InMemorySeasonBudgetStore;
import org.sportsclub.dam.sports_club_manager.infrastructure.player.InMemoryPlayerStore;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowServiceTest {
    @Test
    void financialWorkflowRecordsIncomeExpensesAndPlayerSalary() {
        var factory = Validation.buildDefaultValidatorFactory();
        try {
            var budgetStore = new InMemorySeasonBudgetStore();
            var playerStore = new InMemoryPlayerStore();
            var audit = new InMemoryAuditService();
            var auth = new RoleAuthorizationService();
            var budgetWorkflow = new BudgetWorkflowService(budgetStore, new SeasonBudgetFactory(), auth, audit);
            var validation = new DefaultDomainValidationService(factory.getValidator());
            var transactions = new TransactionWorkflowService(
                    budgetStore, playerStore, validation, auth, audit);

            User financial = new User("finance", "not-used-in-this-test", UserRole.FINANCIAL, null);
            Season season = new Season(2026, 2027);
            budgetWorkflow.create(season, new BigDecimal("500"), financial);

            Player player = new Player("Alex", LocalDate.of(2000, 1, 1), "Forward", 9);
            playerStore.save(player);

            transactions.recordIncome(season, IncomeCategory.OTHER,
                    new BigDecimal("100"), LocalDate.now(), "Sponsor", financial);
            transactions.recordExpense(season, ExpenseCategory.TRAVEL,
                    new BigDecimal("300"), LocalDate.now(), "Trip", financial);
            Transaction salary = transactions.recordPlayerExpense(season, player.getId(),
                    new BigDecimal("200"), LocalDate.now(), "Player salary", financial);

            SeasonBudget savedBudget = budgetStore.findBySeason(season).orElseThrow();
            assertInstanceOf(PlayerExpenseTransaction.class, salary);
            assertEquals(player.getId(), ((PlayerExpenseTransaction) salary).getPlayerId());
            assertEquals(0, savedBudget.getRemaining().compareTo(new BigDecimal("100")));
            assertEquals(4, audit.findAll().size());

            assertThrows(InsufficientBudgetException.class, () -> transactions.recordExpense(
                    season, ExpenseCategory.EQUIPMENT, new BigDecimal("101"),
                    LocalDate.now(), "Over budget", financial));
        } finally {
            factory.close();
        }
    }

    @Test
    void coachCannotCreateBudget() {
        var auth = new RoleAuthorizationService();
        User coach = new User("coach", "hash", UserRole.COACH, null);
        assertThrows(org.sportsclub.dam.sports_club_manager.domain.exception.UnauthorizedOperationException.class,
                () -> auth.require(coach, UserRole.FINANCIAL));
    }
}
