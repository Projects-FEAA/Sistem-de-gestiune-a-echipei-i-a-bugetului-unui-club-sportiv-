package org.sportsclub.dam.sports_club_manager.infrastructure.player;

import org.sportsclub.dam.sports_club_manager.domain.model.Player;
import org.sportsclub.dam.sports_club_manager.infrastructure.persistence.PersistenceMapper;
import org.sportsclub.dam.sports_club_manager.infrastructure.persistence.repository.PlayerJpaRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Primary
public class JpaPlayerStore implements PlayerStore {
    private final PlayerJpaRepository repository;
    private final PersistenceMapper mapper;

    public JpaPlayerStore(PlayerJpaRepository repository, PersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Player save(Player player) {
        return mapper.toDomain(repository.save(mapper.toJpa(player)));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Player> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Player> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByJerseyNumber(Integer number, UUID exceptPlayerId) {
        if (number == null) return false;
        return exceptPlayerId == null
                ? repository.existsByJerseyNumber(number)
                : repository.existsByJerseyNumberAndIdNot(number, exceptPlayerId);
    }
}
