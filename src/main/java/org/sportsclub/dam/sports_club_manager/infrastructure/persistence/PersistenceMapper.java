package org.sportsclub.dam.sports_club_manager.infrastructure.persistence;

import org.sportsclub.dam.sports_club_manager.domain.enums.*;
import org.sportsclub.dam.sports_club_manager.domain.model.*;
import org.sportsclub.dam.sports_club_manager.infrastructure.persistence.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

/** Converts domain aggregates to and from their relational JPA representations. */
@Component
public class PersistenceMapper {
    public PlayerJpaEntity toJpa(Player player) {
        PlayerJpaEntity entity = new PlayerJpaEntity(player.getId(), player.getName(),
                player.getBirthDate(), player.getPosition(), player.getJerseyNumber(),
                player.getStatus(), player.isManuallyInactive());
        for (Contract contract : player.getContracts()) {
            entity.addContract(new ContractJpaEntity(contract.getId(), contract.getContractType(),
                    contract.getOriginClub(), contract.getMonthlySalary(), contract.getTransferFee(),
                    contract.getStartDate(), contract.getEndDate(), contract.getStatus()));
        }
        for (Injury injury : player.getInjuries()) {
            entity.addInjury(new InjuryJpaEntity(injury.getId(), injury.getType(),
                    injury.getStartDate(), injury.getExpectedReturn(), injury.getStatus()));
        }
        return entity;
    }

    public Player toDomain(PlayerJpaEntity entity) {
        List<Contract> contracts = entity.getContracts().stream()
                .map(contract -> Contract.reconstitute(contract.getId(), entity.getId(),
                        contract.getContractType(), contract.getOriginClub(), contract.getMonthlySalary(),
                        contract.getTransferFee(), contract.getStartDate(), contract.getEndDate(),
                        contract.getStatus()))
                .toList();
        List<Injury> injuries = entity.getInjuries().stream()
                .map(injury -> Injury.reconstitute(injury.getId(), entity.getId(), injury.getType(),
                        injury.getStartDate(), injury.getExpectedReturn(), injury.getStatus()))
                .toList();
        return Player.reconstitute(entity.getId(), entity.getName(), entity.getBirthDate(),
                entity.getPosition(), entity.getJerseyNumber(), entity.isManuallyInactive(),
                contracts, injuries);
    }

    public SeasonBudgetJpaEntity toJpa(SeasonBudget budget,
                                       Function<java.util.UUID, PlayerJpaEntity> playerReference) {
        Season season = budget.getSeason();
        SeasonBudgetJpaEntity entity = new SeasonBudgetJpaEntity(budget.getId(),
                new SeasonJpaValue(season.startYear(), season.endYear()), budget.getTotalAmount());
        for (Transaction transaction : budget.getTransactions()) {
            TransactionJpaEntity transactionEntity;
            if (transaction instanceof PlayerExpenseTransaction playerExpense) {
                PlayerExpenseTransactionJpaEntity playerEntity = new PlayerExpenseTransactionJpaEntity(
                        playerExpense.getId(), playerExpense.getStatus(), playerExpense.getAmount(),
                        playerExpense.getDate(), playerExpense.getDescription());
                playerEntity.setPlayer(playerReference.apply(playerExpense.getPlayerId()));
                transactionEntity = playerEntity;
            } else if (transaction instanceof ExpenseTransaction expense) {
                transactionEntity = new ExpenseTransactionJpaEntity(expense.getId(),
                        (ExpenseCategory) expense.getCategory(), expense.getStatus(),
                        expense.getAmount(), expense.getDate(), expense.getDescription());
            } else if (transaction instanceof IncomeTransaction income) {
                transactionEntity = new IncomeTransactionJpaEntity(income.getId(),
                        (IncomeCategory) income.getCategory(), income.getStatus(),
                        income.getAmount(), income.getDate(), income.getDescription());
            } else {
                throw new IllegalArgumentException("Unsupported transaction type: "
                        + transaction.getClass().getName());
            }
            entity.addTransaction(transactionEntity);
        }
        return entity;
    }

    public SeasonBudget toDomain(SeasonBudgetJpaEntity entity) {
        Season season = new Season(entity.getSeason().getStartYear(), entity.getSeason().getEndYear());
        List<Transaction> transactions = entity.getTransactions().stream()
                .map(this::toDomain)
                .toList();
        return SeasonBudget.reconstitute(entity.getId(), season, entity.getTotalAmount(), transactions);
    }

    private Transaction toDomain(TransactionJpaEntity entity) {
        if (entity instanceof PlayerExpenseTransactionJpaEntity playerExpense) {
            return PlayerExpenseTransaction.reconstitute(playerExpense.getId(),
                    entity.getBudget().getId(), playerExpense.getPlayer().getId(),
                    entity.getAmount(), entity.getDate(), entity.getDescription(), entity.getStatus());
        }
        if (entity instanceof ExpenseTransactionJpaEntity expense) {
            return ExpenseTransaction.reconstitute(entity.getId(), entity.getBudget().getId(),
                    expense.getCategory(), entity.getAmount(), entity.getDate(),
                    entity.getDescription(), entity.getStatus());
        }
        if (entity instanceof IncomeTransactionJpaEntity income) {
            return IncomeTransaction.reconstitute(entity.getId(), entity.getBudget().getId(),
                    income.getCategory(), entity.getAmount(), entity.getDate(),
                    entity.getDescription(), entity.getStatus());
        }
        throw new IllegalArgumentException("Unsupported persisted transaction type: "
                + entity.getClass().getName());
    }

    public UserJpaEntity toJpa(User user, PlayerJpaEntity playerReference) {
        return new UserJpaEntity(user.getId(), user.getUsername(), user.getPasswordHash(),
                user.getRole(), playerReference);
    }

    public User toDomain(UserJpaEntity entity) {
        return User.reconstitute(entity.getId(), entity.getUsername(), entity.getPasswordHash(),
                entity.getRole(), entity.getPlayer() == null ? null : entity.getPlayer().getId());
    }
}
