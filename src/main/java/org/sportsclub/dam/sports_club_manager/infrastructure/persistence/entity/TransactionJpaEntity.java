package org.sportsclub.dam.sports_club_manager.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Check;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionStatus;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "transactions", indexes = {
        @Index(name = "idx_transactions_budget", columnList = "season_budget_id"),
        @Index(name = "idx_transactions_status", columnList = "status")
})
@Check(constraints = "transaction_kind <> 'PLAYER_EXPENSE' OR player_id IS NOT NULL")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "transaction_kind", discriminatorType = DiscriminatorType.STRING)
public abstract class TransactionJpaEntity extends AuditedJpaEntity {
    @Id
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "season_budget_id", nullable = false)
    private SeasonBudgetJpaEntity budget;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private TransactionType type;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private TransactionStatus status;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;
    @Column(nullable = false)
    private LocalDate date;
    @Column(length = 500)
    private String description;

    protected TransactionJpaEntity() { }

    protected TransactionJpaEntity(UUID id, TransactionType type, TransactionStatus status,
                                   BigDecimal amount, LocalDate date, String description) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public void setBudget(SeasonBudgetJpaEntity budget) { this.budget = budget; }
    public UUID getId() { return id; }
    public SeasonBudgetJpaEntity getBudget() { return budget; }
    public TransactionType getType() { return type; }
    public TransactionStatus getStatus() { return status; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }
}
