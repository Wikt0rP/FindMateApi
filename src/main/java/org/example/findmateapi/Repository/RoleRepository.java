package org.example.findmateapi.Repository;

import org.example.findmateapi.Entity.ERole;
import org.example.findmateapi.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository  extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
