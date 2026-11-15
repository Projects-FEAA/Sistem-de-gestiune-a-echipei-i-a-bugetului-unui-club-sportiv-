package org.sportsclub.dam.sports_club_manager.infrastructure.user;

import org.sportsclub.dam.sports_club_manager.domain.model.User;
import org.sportsclub.dam.sports_club_manager.infrastructure.persistence.PersistenceMapper;
import org.sportsclub.dam.sports_club_manager.infrastructure.persistence.repository.PlayerJpaRepository;
import org.sportsclub.dam.sports_club_manager.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Repository
@Primary
public class JpaUserStore implements UserStore {
    private final UserJpaRepository repository;
    private final PlayerJpaRepository playerRepository;
    private final PersistenceMapper mapper;

    public JpaUserStore(UserJpaRepository repository, PlayerJpaRepository playerRepository,
                        PersistenceMapper mapper) {
        this.repository = repository;
        this.playerRepository = playerRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public User save(User user) {
        var player = user.getPlayerId() == null
                ? null : playerRepository.getReferenceById(user.getPlayerId());
        return mapper.toDomain(repository.save(mapper.toJpa(user, player)));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return repository.findByUsernameKey(normalize(username)).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return repository.existsByUsernameKey(normalize(username));
    }

    private String normalize(String username) {
        return username == null ? null : username.trim().toLowerCase(Locale.ROOT);
    }
}
