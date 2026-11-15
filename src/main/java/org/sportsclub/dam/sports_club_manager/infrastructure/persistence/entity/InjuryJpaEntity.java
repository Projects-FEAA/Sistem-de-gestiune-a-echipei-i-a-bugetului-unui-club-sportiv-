package org.sportsclub.dam.sports_club_manager.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.sportsclub.dam.sports_club_manager.domain.enums.InjuryStatus;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "injuries", indexes = {
        @Index(name = "idx_injuries_player_status", columnList = "player_id,status")
})
public class InjuryJpaEntity extends AuditedJpaEntity {
    @Id
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private PlayerJpaEntity player;
    @Column(nullable = false, length = 120)
    private String type;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "expected_return_date", nullable = false)
    private LocalDate expectedReturn;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private InjuryStatus status;

    protected InjuryJpaEntity() { }

    public InjuryJpaEntity(UUID id, String type, LocalDate startDate,
                           LocalDate expectedReturn, InjuryStatus status) {
        this.id = id;
        this.type = type;
        this.startDate = startDate;
        this.expectedReturn = expectedReturn;
        this.status = status;
    }

    public void setPlayer(PlayerJpaEntity player) { this.player = player; }
    public UUID getId() { return id; }
    public PlayerJpaEntity getPlayer() { return player; }
    public String getType() { return type; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getExpectedReturn() { return expectedReturn; }
    public InjuryStatus getStatus() { return status; }
}
