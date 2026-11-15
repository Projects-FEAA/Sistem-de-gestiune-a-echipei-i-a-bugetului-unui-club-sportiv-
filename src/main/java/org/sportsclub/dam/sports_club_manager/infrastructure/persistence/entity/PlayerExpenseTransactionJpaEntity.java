package org.sportsclub.dam.sports_club_manager.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@DiscriminatorValue("PLAYER_EXPENSE")
public class PlayerExpenseTransactionJpaEntity extends ExpenseTransactionJpaEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id")
    private PlayerJpaEntity player;

    protected PlayerExpenseTransactionJpaEntity() { }

    public PlayerExpenseTransactionJpaEntity(UUID id, TransactionStatus status, BigDecimal amount,
                                             LocalDate date, String description) {
        super(id, org.sportsclub.dam.sports_club_manager.domain.enums.ExpenseCategory.SALARIES,
                status, amount, date, description);
    }

    public void setPlayer(PlayerJpaEntity player) { this.player = player; }
    public PlayerJpaEntity getPlayer() { return player; }
}
