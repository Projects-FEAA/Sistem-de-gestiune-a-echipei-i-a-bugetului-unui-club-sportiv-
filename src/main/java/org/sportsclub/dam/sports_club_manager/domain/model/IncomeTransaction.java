package org.sportsclub.dam.sports_club_manager.domain.model;

import org.sportsclub.dam.sports_club_manager.domain.enums.IncomeCategory;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionStatus;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class IncomeTransaction extends Transaction {
    public IncomeTransaction(UUID seasonBudgetId, IncomeCategory category,
                              BigDecimal amount, LocalDate date, String description) {
        super(seasonBudgetId, TransactionType.INCOME, category, amount, date, description);
    }

    private IncomeTransaction(UUID id, UUID seasonBudgetId, IncomeCategory category,
                              BigDecimal amount, LocalDate date, String description,
                              TransactionStatus status) {
        super(id, seasonBudgetId, TransactionType.INCOME, category, amount, date, description, status);
    }

    public static IncomeTransaction reconstitute(UUID id, UUID seasonBudgetId,
            IncomeCategory category, BigDecimal amount, LocalDate date, String description,
            TransactionStatus status) {
        return new IncomeTransaction(id, seasonBudgetId, category, amount, date, description, status);
    }
}
