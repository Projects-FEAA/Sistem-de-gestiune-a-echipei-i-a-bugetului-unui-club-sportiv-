package org.sportsclub.dam.sports_club_manager.service.computation;

import org.sportsclub.dam.sports_club_manager.domain.enums.ExpenseCategory;
import org.sportsclub.dam.sports_club_manager.domain.enums.IncomeCategory;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionStatus;
import org.sportsclub.dam.sports_club_manager.domain.model.ExpenseTransaction;
import org.sportsclub.dam.sports_club_manager.domain.model.IncomeTransaction;
import org.sportsclub.dam.sports_club_manager.domain.model.SeasonBudget;
import org.sportsclub.dam.sports_club_manager.domain.model.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

@Service
public class BudgetReportComputationService implements BudgetReportComputation {
    private final BudgetComputationService computation;

    public BudgetReportComputationService(BudgetComputationService computation) {
        this.computation = computation;
    }

    @Override
    public BudgetReport compute(SeasonBudget budget) {
        Map<IncomeCategory, BigDecimal> incomeByCategory = emptyIncomeTotals();
        Map<ExpenseCategory, BigDecimal> expensesByCategory = emptyExpenseTotals();

        for (Transaction transaction : budget.getTransactions()) {
            if (transaction.getStatus() != TransactionStatus.ACTIVE) {
                continue;
            }
            if (transaction instanceof IncomeTransaction income) {
                incomeByCategory.merge((IncomeCategory) income.getCategory(),
                        income.getAmount(), BigDecimal::add);
            } else if (transaction instanceof ExpenseTransaction expense) {
                expensesByCategory.merge((ExpenseCategory) expense.getCategory(),
                        expense.getAmount(), BigDecimal::add);
            }
        }

        return new BudgetReport(
                budget.getSeason(),
                budget.getTotalAmount(),
                computation.income(budget),
                computation.spent(budget),
                computation.remaining(budget),
                Collections.unmodifiableMap(incomeByCategory),
                Collections.unmodifiableMap(expensesByCategory));
    }

    private Map<IncomeCategory, BigDecimal> emptyIncomeTotals() {
        Map<IncomeCategory, BigDecimal> totals = new EnumMap<>(IncomeCategory.class);
        for (IncomeCategory category : IncomeCategory.values()) {
            totals.put(category, BigDecimal.ZERO);
        }
        return totals;
    }

    private Map<ExpenseCategory, BigDecimal> emptyExpenseTotals() {
        Map<ExpenseCategory, BigDecimal> totals = new EnumMap<>(ExpenseCategory.class);
        for (ExpenseCategory category : ExpenseCategory.values()) {
            totals.put(category, BigDecimal.ZERO);
        }
        return totals;
    }
}
