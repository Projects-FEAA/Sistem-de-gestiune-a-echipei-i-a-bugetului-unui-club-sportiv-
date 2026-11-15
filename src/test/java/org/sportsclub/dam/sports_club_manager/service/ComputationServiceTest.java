package org.sportsclub.dam.sports_club_manager.service;

import org.junit.jupiter.api.Test;
import org.sportsclub.dam.sports_club_manager.domain.enums.ExpenseCategory;
import org.sportsclub.dam.sports_club_manager.domain.enums.IncomeCategory;
import org.sportsclub.dam.sports_club_manager.domain.model.*;
import org.sportsclub.dam.sports_club_manager.service.computation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ComputationServiceTest {
    @Test
    void contractCostUsesAnnualSalaryAndTransferFee() {
        Contract contract = new Contract(UUID.randomUUID(), org.sportsclub.dam.sports_club_manager.domain.enums.ContractType.PERMANENT,
                null, new BigDecimal("1200"), new BigDecimal("5000"),
                LocalDate.of(2026, 7, 1), null);
        ContractCostComputationService service = new ContractCostComputationService();
        assertEquals(0, service.annualSalary(contract).compareTo(new BigDecimal("14400")));
        assertEquals(0, service.activationCost(contract).compareTo(new BigDecimal("19400")));
    }

    @Test
    void budgetReportSeparatesIncomeAndExpenseCategoriesAndIgnoresReversedTransactions() {
        SeasonBudget budget = new SeasonBudget(new Season(2026, 2027), new BigDecimal("1000"));
        budget.addTransaction(new IncomeTransaction(budget.getId(), IncomeCategory.OTHER,
                new BigDecimal("250"), LocalDate.now(), "Sponsor"));
        budget.addTransaction(new IncomeTransaction(budget.getId(), IncomeCategory.TICKETS,
                new BigDecimal("50"), LocalDate.now(), "Tickets"));
        budget.addTransaction(new ExpenseTransaction(budget.getId(), ExpenseCategory.TRAVEL,
                new BigDecimal("100"), LocalDate.now(), "Travel"));

        ExpenseTransaction reversedExpense = new ExpenseTransaction(budget.getId(), ExpenseCategory.EQUIPMENT,
                new BigDecimal("25"), LocalDate.now(), "Reversed purchase");
        budget.addTransaction(reversedExpense);
        budget.reverseTransaction(reversedExpense.getId());

        BudgetReportComputationService reports =
                new BudgetReportComputationService(new BudgetComputationService());
        BudgetReport report = reports.compute(budget);

        assertEquals(0, report.remaining().compareTo(new BigDecimal("1200")));
        assertEquals(0, report.incomeByCategory().get(IncomeCategory.OTHER).compareTo(new BigDecimal("250")));
        assertEquals(0, report.incomeByCategory().get(IncomeCategory.TICKETS).compareTo(new BigDecimal("50")));
        assertEquals(0, report.expensesByCategory().get(ExpenseCategory.TRAVEL).compareTo(new BigDecimal("100")));
        assertEquals(0, report.expensesByCategory().get(ExpenseCategory.EQUIPMENT).compareTo(BigDecimal.ZERO));
        assertThrows(UnsupportedOperationException.class,
                () -> report.incomeByCategory().put(IncomeCategory.OTHER, BigDecimal.ZERO));
    }

    @Test
    void playerExpenseIsAlsoAnExpenseAndRetainsPlayerAssociation() {
        UUID playerId = UUID.randomUUID();
        SeasonBudget budget = new SeasonBudget(new Season(2026, 2027), new BigDecimal("2000"));
        PlayerExpenseTransaction salary = new PlayerExpenseTransaction(
                budget.getId(), playerId, new BigDecimal("500"), LocalDate.now(), "Monthly salary");
        budget.addTransaction(salary);

        assertEquals(playerId, salary.getPlayerId());
        assertEquals(0, budget.getSpent().compareTo(new BigDecimal("500")));
        assertEquals(0, salary.getCategory().name().compareTo("SALARIES"));
    }
}
