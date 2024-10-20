package org.example.findmateapi.Repository;

import org.example.findmateapi.Entity.Cs2Profile;
import org.example.findmateapi.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Cs2ProfileRepository extends JpaRepository<Cs2Profile, Long> {
    Optional<Cs2Profile> findByUser(User user);
}
