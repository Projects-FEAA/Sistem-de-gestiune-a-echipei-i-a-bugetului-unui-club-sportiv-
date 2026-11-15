package org.sportsclub.dam.sports_club_manager.service.workflow;

import org.sportsclub.dam.sports_club_manager.domain.enums.ExpenseCategory;
import org.sportsclub.dam.sports_club_manager.domain.enums.IncomeCategory;
import org.sportsclub.dam.sports_club_manager.domain.enums.UserRole;
import org.sportsclub.dam.sports_club_manager.domain.model.*;
import org.sportsclub.dam.sports_club_manager.domain.exception.EntityNotFoundException;
import org.sportsclub.dam.sports_club_manager.service.audit.AuditAction;
import org.sportsclub.dam.sports_club_manager.service.audit.AuditService;
import org.sportsclub.dam.sports_club_manager.service.validation.DomainValidationService;
import org.sportsclub.dam.sports_club_manager.service.validation.RoleAuthorizationService;
import org.sportsclub.dam.sports_club_manager.infrastructure.budget.SeasonBudgetStore;
import org.sportsclub.dam.sports_club_manager.infrastructure.player.PlayerStore;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class TransactionWorkflowService implements TransactionWorkflow {
    private final SeasonBudgetStore budgets;
    private final PlayerStore players;
    private final DomainValidationService validation;
    private final RoleAuthorizationService authorization;
    private final AuditService audit;

    public TransactionWorkflowService(SeasonBudgetStore budgets, PlayerStore players,
                                      DomainValidationService validation,
                                      RoleAuthorizationService authorization,
                                      AuditService audit) {
        this.budgets = budgets;
        this.players = players;
        this.validation = validation;
        this.authorization = authorization;
        this.audit = audit;
    }

    private SeasonBudget budget(Season season) {
        return budgets.findBySeason(season)
                .orElseThrow(() -> new EntityNotFoundException("Budget not found for season " + season));
    }

    @Override
    public Transaction recordIncome(Season season, IncomeCategory category, BigDecimal amount,
                                    LocalDate date, String description, User actor) {
        authorization.require(actor, UserRole.FINANCIAL);
        SeasonBudget budget = budget(season);
        IncomeTransaction transaction = new IncomeTransaction(
                budget.getId(), category, amount, date, description);
        return save(budget, transaction, actor, "Income " + amount);
    }

    @Override
    public Transaction recordExpense(Season season, ExpenseCategory category, BigDecimal amount,
                                     LocalDate date, String description, User actor) {
        authorization.require(actor, UserRole.FINANCIAL);
        SeasonBudget budget = budget(season);
        ExpenseTransaction transaction = new ExpenseTransaction(
                budget.getId(), category, amount, date, description);
        return save(budget, transaction, actor, "Expense " + amount);
    }

    @Override
    public Transaction recordPlayerExpense(Season season, UUID playerId, BigDecimal amount,
                                           LocalDate date, String description, User actor) {
        authorization.require(actor, UserRole.FINANCIAL);
        players.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player not found: " + playerId));
        SeasonBudget budget = budget(season);
        PlayerExpenseTransaction transaction = new PlayerExpenseTransaction(
                budget.getId(), playerId, amount, date, description);
        return save(budget, transaction, actor, "Player salary expense " + amount + " for " + playerId);
    }

    private Transaction save(SeasonBudget budget, Transaction transaction, User actor, String details) {
        validation.validate(transaction);
        budget.addTransaction(transaction);
        budgets.save(budget);
        audit.record(actor, AuditAction.TRANSACTION_RECORDED,
                transaction.getClass().getSimpleName(), transaction.getId(), details);
        return transaction;
    }

    @Override
    public Transaction reverse(Season season, UUID transactionId, User actor) {
        authorization.require(actor, UserRole.FINANCIAL);
        SeasonBudget budget = budget(season);
        budget.reverseTransaction(transactionId);
        budgets.save(budget);
        Transaction transaction = budget.getTransactions().stream()
                .filter(item -> item.getId().equals(transactionId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found: " + transactionId));
        audit.record(actor, AuditAction.TRANSACTION_REVERSED,
                transaction.getClass().getSimpleName(), transactionId, "Transaction reversed");
        return transaction;
    }
}
