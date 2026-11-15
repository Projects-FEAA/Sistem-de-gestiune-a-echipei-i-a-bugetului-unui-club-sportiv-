package org.sportsclub.dam.sports_club_manager.infrastructure.user;
import org.sportsclub.dam.sports_club_manager.domain.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Profile;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@Repository
@Profile("in-memory")
public class InMemoryUserStore implements UserStore {
    private final Map<UUID, User> users = new ConcurrentHashMap<>();
    @Override public User save(User user) { users.put(user.getId(), user); return user; }
    @Override public Optional<User> findById(UUID id) { return Optional.ofNullable(users.get(id)); }
    @Override public Optional<User> findByUsername(String username) {
        return users.values().stream().filter(u -> u.getUsername().equalsIgnoreCase(username)).findFirst();
    }
    @Override public boolean existsByUsername(String username) { return findByUsername(username).isPresent(); }
}
