package org.example.findmateapi.Repository;

import org.example.findmateapi.Entity.Cs2Profile;
import org.example.findmateapi.Entity.LolProfile;
import org.example.findmateapi.Entity.UserProfiles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LolProfileRepository extends JpaRepository<LolProfile, Long>, LolProfileRepositoryCustom {
    Optional<LolProfile> findByUserProfiles(UserProfiles userProfiles);
}
