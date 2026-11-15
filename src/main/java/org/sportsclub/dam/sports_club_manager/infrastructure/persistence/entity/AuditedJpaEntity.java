package org.sportsclub.dam.sports_club_manager.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.sportsclub.dam.sports_club_manager.infrastructure.persistence.listener.PersistenceAuditListener;

import java.time.Instant;

@MappedSuperclass
@EntityListeners(PersistenceAuditListener.class)
public abstract class AuditedJpaEntity {
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public void markCreated(Instant timestamp) {
        if (createdAt == null) createdAt = timestamp;
        updatedAt = timestamp;
    }

    public void markUpdated(Instant timestamp) {
        updatedAt = timestamp;
    }

    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
