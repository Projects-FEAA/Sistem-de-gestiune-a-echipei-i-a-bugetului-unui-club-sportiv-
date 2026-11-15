package org.sportsclub.dam.sports_club_manager.service.workflow;

import org.sportsclub.dam.sports_club_manager.domain.enums.ExpenseCategory;
import org.sportsclub.dam.sports_club_manager.domain.enums.IncomeCategory;
import org.sportsclub.dam.sports_club_manager.domain.model.Season;
import org.sportsclub.dam.sports_club_manager.domain.model.Transaction;
import org.sportsclub.dam.sports_club_manager.domain.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface TransactionWorkflow {
    Transaction recordIncome(Season season, IncomeCategory category, BigDecimal amount,
                             LocalDate date, String description, User actor);

    Transaction recordExpense(Season season, ExpenseCategory category, BigDecimal amount,
                              LocalDate date, String description, User actor);

    Transaction recordPlayerExpense(Season season, UUID playerId, BigDecimal amount,
                                    LocalDate date, String description, User actor);

    Transaction reverse(Season season, UUID transactionId, User actor);
}
