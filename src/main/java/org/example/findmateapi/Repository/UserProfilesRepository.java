package org.example.findmateapi.Repository;

import org.example.findmateapi.Entity.User;
import org.example.findmateapi.Entity.UserProfiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfilesRepository extends JpaRepository<UserProfiles, Long> {
    Optional<UserProfiles> findByUser(User user);
}
