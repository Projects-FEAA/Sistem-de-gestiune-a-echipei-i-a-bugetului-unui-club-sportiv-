package org.sportsclub.dam.sports_club_manager.domain.model;

import org.sportsclub.dam.sports_club_manager.domain.enums.ExpenseCategory;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionStatus;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class ExpenseTransaction extends Transaction {
    public ExpenseTransaction(UUID seasonBudgetId, ExpenseCategory category,
                               BigDecimal amount, LocalDate date, String description) {
        super(seasonBudgetId, TransactionType.EXPENSE, category, amount, date, description);
    }

    protected ExpenseTransaction(UUID id, UUID seasonBudgetId, ExpenseCategory category,
                                 BigDecimal amount, LocalDate date, String description,
                                 TransactionStatus status) {
        super(id, seasonBudgetId, TransactionType.EXPENSE, category, amount, date, description, status);
    }

    public static ExpenseTransaction reconstitute(UUID id, UUID seasonBudgetId,
            ExpenseCategory category, BigDecimal amount, LocalDate date, String description,
            TransactionStatus status) {
        return new ExpenseTransaction(id, seasonBudgetId, category, amount, date, description, status);
    }
}
