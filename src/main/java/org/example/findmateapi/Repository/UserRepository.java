package org.example.findmateapi.Repository;

import org.example.findmateapi.Entity.User;
import org.example.findmateapi.Entity.UserProfiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findUserById(Long id);

    Optional<User> findUserByUserProfiles(UserProfiles userProfiles);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);


}
