package org.sportsclub.dam.sports_club_manager.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.sportsclub.dam.sports_club_manager.domain.enums.UserRole;

import java.util.Locale;
import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uk_users_username_normalized", columnNames = "username_normalized")
})
public class UserJpaEntity extends AuditedJpaEntity {
    @Id
    private UUID id;
    @Column(nullable = false, length = 100)
    private String username;
    @Column(name = "username_normalized", nullable = false, length = 100)
    private String usernameKey;
    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private UserRole role;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private PlayerJpaEntity player;

    protected UserJpaEntity() { }

    public UserJpaEntity(UUID id, String username, String passwordHash, UserRole role,
                         PlayerJpaEntity player) {
        this.id = id;
        this.username = username;
        this.usernameKey = username == null ? null : username.trim().toLowerCase(Locale.ROOT);
        this.passwordHash = passwordHash;
        this.role = role;
        this.player = player;
    }

    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public String getUsernameKey() { return usernameKey; }
    public String getPasswordHash() { return passwordHash; }
    public UserRole getRole() { return role; }
    public PlayerJpaEntity getPlayer() { return player; }
}
