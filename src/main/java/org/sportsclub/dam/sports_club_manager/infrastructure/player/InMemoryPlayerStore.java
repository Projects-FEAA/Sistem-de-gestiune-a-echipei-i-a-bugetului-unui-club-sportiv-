package org.sportsclub.dam.sports_club_manager.infrastructure.player;
import org.sportsclub.dam.sports_club_manager.domain.model.Player;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Profile;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@Repository
@Profile("in-memory")
public class InMemoryPlayerStore implements PlayerStore {
    private final Map<UUID, Player> players = new ConcurrentHashMap<>();
    @Override public Player save(Player player) { players.put(player.getId(), player); return player; }
    @Override public Optional<Player> findById(UUID id) { return Optional.ofNullable(players.get(id)); }
    @Override public List<Player> findAll() { return List.copyOf(players.values()); }
    @Override public boolean existsByJerseyNumber(Integer number, UUID exceptId) {
        if (number == null) return false;
        return players.values().stream().anyMatch(p -> number.equals(p.getJerseyNumber()) && !p.getId().equals(exceptId));
    }
}
