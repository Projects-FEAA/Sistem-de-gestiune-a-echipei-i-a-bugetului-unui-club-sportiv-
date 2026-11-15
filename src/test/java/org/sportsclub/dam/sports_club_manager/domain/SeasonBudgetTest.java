package org.sportsclub.dam.sports_club_manager.domain;

import org.junit.jupiter.api.Test;
import org.sportsclub.dam.sports_club_manager.domain.enums.ExpenseCategory;
import org.sportsclub.dam.sports_club_manager.domain.enums.IncomeCategory;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionStatus;
import org.sportsclub.dam.sports_club_manager.domain.exception.InsufficientBudgetException;
import org.sportsclub.dam.sports_club_manager.domain.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SeasonBudgetTest {
    @Test
    void incomeExpensesAndReversalAreIncludedInCalculations() {
        SeasonBudget budget = new SeasonBudget(new Season(2026, 2027), new BigDecimal("1000"));
        IncomeTransaction income = new IncomeTransaction(
                budget.getId(), IncomeCategory.OTHER, new BigDecimal("200"), LocalDate.now(), "Sponsor");
        budget.addTransaction(income);

        ExpenseTransaction expense = new ExpenseTransaction(
                budget.getId(), ExpenseCategory.TRAVEL, new BigDecimal("300"), LocalDate.now(), "Travel");
        budget.addTransaction(expense);

        assertEquals(0, budget.getIncome().compareTo(new BigDecimal("200")));
        assertEquals(0, budget.getSpent().compareTo(new BigDecimal("300")));
        assertEquals(0, budget.getRemaining().compareTo(new BigDecimal("900")));

        budget.reverseTransaction(expense.getId());
        assertEquals(0, budget.getSpent().compareTo(BigDecimal.ZERO));
        assertEquals(0, budget.getRemaining().compareTo(new BigDecimal("1200")));
        assertEquals(TransactionStatus.REVERSED, expense.getStatus());
        assertEquals(2, budget.getTransactions().size());
    }

    @Test
    void expenseCannotExceedRemainingBudgetButCanEqualIt() {
        SeasonBudget budget = new SeasonBudget(new Season(2026, 2027), new BigDecimal("100"));
        assertThrows(InsufficientBudgetException.class, () -> budget.addTransaction(
                new ExpenseTransaction(budget.getId(), ExpenseCategory.OTHER,
                        new BigDecimal("100.01"), LocalDate.now(), "Too much")));

        budget.addTransaction(new ExpenseTransaction(budget.getId(), ExpenseCategory.OTHER,
                new BigDecimal("100"), LocalDate.now(), "Exact amount"));
        assertEquals(0, budget.getRemaining().compareTo(BigDecimal.ZERO));
    }
}
