package org.sportsclub.dam.sports_club_manager.domain.model;

import jakarta.validation.constraints.NotNull;
import org.sportsclub.dam.sports_club_manager.domain.enums.ExpenseCategory;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class PlayerExpenseTransaction extends ExpenseTransaction {
    @NotNull
    private final UUID playerId;

    public PlayerExpenseTransaction(UUID seasonBudgetId, UUID playerId,
                                    BigDecimal amount, LocalDate date, String description) {
        this(UUID.randomUUID(), seasonBudgetId, playerId, amount, date, description,
                TransactionStatus.ACTIVE);
    }

    private PlayerExpenseTransaction(UUID id, UUID seasonBudgetId, UUID playerId,
                                     BigDecimal amount, LocalDate date, String description,
                                     TransactionStatus status) {
        super(id, seasonBudgetId, ExpenseCategory.SALARIES, amount, date, description, status);
        this.playerId = playerId;
    }

    public static PlayerExpenseTransaction reconstitute(UUID id, UUID seasonBudgetId,
            UUID playerId, BigDecimal amount, LocalDate date, String description,
            TransactionStatus status) {
        return new PlayerExpenseTransaction(id, seasonBudgetId, playerId, amount, date, description, status);
    }

    @Override
    public boolean hasValidFields() {
        return playerId != null && super.hasValidFields();
    }

    public UUID getPlayerId() { return playerId; }
}
