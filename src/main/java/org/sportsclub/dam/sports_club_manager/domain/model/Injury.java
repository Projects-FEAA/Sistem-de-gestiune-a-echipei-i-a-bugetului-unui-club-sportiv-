package org.sportsclub.dam.sports_club_manager.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.sportsclub.dam.sports_club_manager.domain.enums.InjuryStatus;
import org.sportsclub.dam.sports_club_manager.domain.exception.InvalidStateTransitionException;

import java.time.LocalDate;
import java.util.UUID;

public class Injury {
    private final UUID id;
    @NotNull private final UUID playerId;
    @NotBlank private final String type;
    @NotNull private final LocalDate startDate;
    @NotNull private final LocalDate expectedReturn;
    @NotNull private InjuryStatus status;

    public Injury(UUID playerId, String type, LocalDate startDate, LocalDate expectedReturn) {
        this(UUID.randomUUID(), playerId, type, startDate, expectedReturn, InjuryStatus.ACTIVE);
    }

    private Injury(UUID id, UUID playerId, String type, LocalDate startDate,
                   LocalDate expectedReturn, InjuryStatus status) {
        this.id = id;
        this.playerId = playerId;
        this.type = type == null ? null : type.trim();
        this.startDate = startDate;
        this.expectedReturn = expectedReturn;
        this.status = status;
    }

    public static Injury reconstitute(UUID id, UUID playerId, String type,
                                      LocalDate startDate, LocalDate expectedReturn,
                                      InjuryStatus status) {
        return new Injury(id, playerId, type, startDate, expectedReturn, status);
    }

    public boolean hasValidDates() {
        return playerId != null && type != null && !type.isBlank() &&
                startDate != null && expectedReturn != null && expectedReturn.isAfter(startDate)
                && status != null;
    }
    public void recover() {
        if (status != InjuryStatus.ACTIVE)
            throw new InvalidStateTransitionException("Only an active injury can be recovered.");
        status = InjuryStatus.RECOVERED;
    }
    public UUID getId() { return id; }
    public UUID getPlayerId() { return playerId; }
    public String getType() { return type; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getExpectedReturn() { return expectedReturn; }
    public InjuryStatus getStatus() { return status; }
}
