package org.sportsclub.dam.sports_club_manager.infrastructure.persistence.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import org.sportsclub.dam.sports_club_manager.infrastructure.persistence.entity.AuditedJpaEntity;

import java.time.Instant;
import java.util.logging.Logger;

/** Adds persistence timestamps and reports entity removals to the application log. */
public class PersistenceAuditListener {
    private static final Logger LOGGER =
            Logger.getLogger(PersistenceAuditListener.class.getName());

    @PrePersist
    public void beforeInsert(AuditedJpaEntity entity) {
        entity.markCreated(Instant.now());
    }

    @PreUpdate
    public void beforeUpdate(AuditedJpaEntity entity) {
        entity.markUpdated(Instant.now());
    }

    @PreRemove
    public void beforeDelete(AuditedJpaEntity entity) {
        LOGGER.info(() -> "Removing persisted entity " + entity.getClass().getSimpleName()
                + " created at " + entity.getCreatedAt());
    }
}
