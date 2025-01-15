package org.example.findmateapi.Repository;

import org.example.findmateapi.Entity.SportProfile;
import org.example.findmateapi.Entity.UserProfiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SportProfileRepository extends JpaRepository<SportProfile, Long>, SportProfileRepositoryCustom {
    Optional<SportProfile> findSportProfileByUserProfiles(UserProfiles userProfiles);
}
