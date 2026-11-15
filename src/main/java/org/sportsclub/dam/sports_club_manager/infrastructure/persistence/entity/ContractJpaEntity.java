package org.sportsclub.dam.sports_club_manager.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.sportsclub.dam.sports_club_manager.domain.enums.ContractStatus;
import org.sportsclub.dam.sports_club_manager.domain.enums.ContractType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "contracts", indexes = {
        @Index(name = "idx_contracts_player", columnList = "player_id")
})
public class ContractJpaEntity extends AuditedJpaEntity {
    @Id
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private PlayerJpaEntity player;
    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type", nullable = false, length = 24)
    private ContractType contractType;
    @Column(name = "origin_club", length = 150)
    private String originClub;
    @Column(name = "monthly_salary", nullable = false, precision = 14, scale = 2)
    private BigDecimal monthlySalary;
    @Column(name = "transfer_fee", nullable = false, precision = 14, scale = 2)
    private BigDecimal transferFee;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private ContractStatus status;

    protected ContractJpaEntity() { }

    public ContractJpaEntity(UUID id, ContractType contractType, String originClub,
                             BigDecimal monthlySalary, BigDecimal transferFee,
                             LocalDate startDate, LocalDate endDate, ContractStatus status) {
        this.id = id;
        this.contractType = contractType;
        this.originClub = originClub;
        this.monthlySalary = monthlySalary;
        this.transferFee = transferFee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public void setPlayer(PlayerJpaEntity player) { this.player = player; }
    public UUID getId() { return id; }
    public PlayerJpaEntity getPlayer() { return player; }
    public ContractType getContractType() { return contractType; }
    public String getOriginClub() { return originClub; }
    public BigDecimal getMonthlySalary() { return monthlySalary; }
    public BigDecimal getTransferFee() { return transferFee; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public ContractStatus getStatus() { return status; }
}
