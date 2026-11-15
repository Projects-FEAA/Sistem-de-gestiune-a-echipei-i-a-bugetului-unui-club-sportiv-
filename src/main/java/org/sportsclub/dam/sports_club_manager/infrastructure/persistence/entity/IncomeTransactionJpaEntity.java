package org.sportsclub.dam.sports_club_manager.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.sportsclub.dam.sports_club_manager.domain.enums.IncomeCategory;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionStatus;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@DiscriminatorValue("INCOME")
public class IncomeTransactionJpaEntity extends TransactionJpaEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "income_category", length = 32)
    private IncomeCategory category;

    protected IncomeTransactionJpaEntity() { }

    public IncomeTransactionJpaEntity(UUID id, IncomeCategory category, TransactionStatus status,
                                       BigDecimal amount, LocalDate date, String description) {
        super(id, TransactionType.INCOME, status, amount, date, description);
        this.category = category;
    }

    public IncomeCategory getCategory() { return category; }
}
