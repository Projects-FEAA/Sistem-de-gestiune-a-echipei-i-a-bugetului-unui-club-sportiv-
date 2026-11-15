package org.sportsclub.dam.sports_club_manager.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.sportsclub.dam.sports_club_manager.domain.enums.ExpenseCategory;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionStatus;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@DiscriminatorValue("EXPENSE")
public class ExpenseTransactionJpaEntity extends TransactionJpaEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "expense_category", length = 32)
    private ExpenseCategory category;

    protected ExpenseTransactionJpaEntity() { }

    public ExpenseTransactionJpaEntity(UUID id, ExpenseCategory category, TransactionStatus status,
                                        BigDecimal amount, LocalDate date, String description) {
        super(id, TransactionType.EXPENSE, status, amount, date, description);
        this.category = category;
    }

    public ExpenseCategory getCategory() { return category; }
}
