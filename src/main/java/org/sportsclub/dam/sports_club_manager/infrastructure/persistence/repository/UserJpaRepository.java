package org.sportsclub.dam.sports_club_manager.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sportsclub.dam.sports_club_manager.infrastructure.persistence.entity.UserJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    Optional<UserJpaEntity> findByUsernameKey(String usernameKey);
    boolean existsByUsernameKey(String usernameKey);
}
