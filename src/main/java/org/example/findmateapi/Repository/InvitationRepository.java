package org.example.findmateapi.Repository;

import org.example.findmateapi.Entity.Invitation;
import org.example.findmateapi.Entity.Team;
import org.example.findmateapi.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findAllByReceiver(User receiver);
    List<Invitation> findAllByTeam(Team team);
    Invitation findByReceiverAndTeam(User receiver, Team team);
    void deleteByReceiverAndTeam(User receiver, Team team);
}
