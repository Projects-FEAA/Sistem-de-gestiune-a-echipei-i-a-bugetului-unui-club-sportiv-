package org.sportsclub.dam.sports_club_manager.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.sportsclub.dam.sports_club_manager.domain.enums.PlayerStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "players", uniqueConstraints = {
        @UniqueConstraint(name = "uk_players_jersey_number", columnNames = "jersey_number")
})
public class PlayerJpaEntity extends AuditedJpaEntity {
    @Id
    private UUID id;
    @Column(nullable = false, length = 120)
    private String name;
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;
    @Column(nullable = false, length = 80)
    private String position;
    @Column(name = "jersey_number")
    private Integer jerseyNumber;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private PlayerStatus status;
    @Column(name = "manually_inactive", nullable = false)
    private boolean manuallyInactive;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractJpaEntity> contracts = new ArrayList<>();

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InjuryJpaEntity> injuries = new ArrayList<>();

    protected PlayerJpaEntity() { }

    public PlayerJpaEntity(UUID id, String name, LocalDate birthDate, String position,
                           Integer jerseyNumber, PlayerStatus status, boolean manuallyInactive) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.position = position;
        this.jerseyNumber = jerseyNumber;
        this.status = status;
        this.manuallyInactive = manuallyInactive;
    }

    public void addContract(ContractJpaEntity contract) {
        contracts.add(contract);
        contract.setPlayer(this);
    }

    public void addInjury(InjuryJpaEntity injury) {
        injuries.add(injury);
        injury.setPlayer(this);
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getPosition() { return position; }
    public Integer getJerseyNumber() { return jerseyNumber; }
    public PlayerStatus getStatus() { return status; }
    public boolean isManuallyInactive() { return manuallyInactive; }
    public List<ContractJpaEntity> getContracts() { return contracts; }
    public List<InjuryJpaEntity> getInjuries() { return injuries; }
}
