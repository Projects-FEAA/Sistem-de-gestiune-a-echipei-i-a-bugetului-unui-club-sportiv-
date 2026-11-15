package org.sportsclub.dam.sports_club_manager.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "season_budgets", uniqueConstraints = {
        @UniqueConstraint(name = "uk_budget_season", columnNames = {"season_start_year", "season_end_year"})
})
public class SeasonBudgetJpaEntity extends AuditedJpaEntity {
    @Id
    private UUID id;
    @Embedded
    private SeasonJpaValue season;
    @Column(name = "total_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionJpaEntity> transactions = new ArrayList<>();

    protected SeasonBudgetJpaEntity() { }

    public SeasonBudgetJpaEntity(UUID id, SeasonJpaValue season, BigDecimal totalAmount) {
        this.id = id;
        this.season = season;
        this.totalAmount = totalAmount;
    }

    public void addTransaction(TransactionJpaEntity transaction) {
        transactions.add(transaction);
        transaction.setBudget(this);
    }

    public UUID getId() { return id; }
    public SeasonJpaValue getSeason() { return season; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public List<TransactionJpaEntity> getTransactions() { return transactions; }
}
