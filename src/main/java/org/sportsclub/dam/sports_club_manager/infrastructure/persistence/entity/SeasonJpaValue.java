package org.sportsclub.dam.sports_club_manager.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

/** Relational representation of the Season value object. */
@Embeddable
public class SeasonJpaValue {
    @Column(name = "season_start_year", nullable = false)
    private int startYear;
    @Column(name = "season_end_year", nullable = false)
    private int endYear;

    protected SeasonJpaValue() { }

    public SeasonJpaValue(int startYear, int endYear) {
        this.startYear = startYear;
        this.endYear = endYear;
    }

    public int getStartYear() { return startYear; }
    public int getEndYear() { return endYear; }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof SeasonJpaValue that)) return false;
        return startYear == that.startYear && endYear == that.endYear;
    }

    @Override
    public int hashCode() { return Objects.hash(startYear, endYear); }
}
