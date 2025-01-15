package org.example.findmateapi.Service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.findmateapi.Component.UserComponent;
import org.example.findmateapi.Entity.Invitation;
import org.example.findmateapi.Entity.Team;
import org.example.findmateapi.Entity.User;
import org.example.findmateapi.Repository.InvitationRepository;
import org.example.findmateapi.Repository.TeamRepository;
import org.example.findmateapi.Repository.UserRepository;
import org.example.findmateapi.Request.SendInvitationRequest;
import org.example.findmateapi.Response.UserValidationResponse;
import org.example.findmateapi.Response.ViewInvitationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
public class InvitationService {

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(InvitationService.class);


    public ResponseEntity<?> createInvitation(SendInvitationRequest sendInvitationRequest, HttpServletRequest request) {

        UserValidationResponse userValidation = userComponent.getUserFromRequest(request);
        if(!userValidation.getStatus().equals("OK")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userValidation.getStatus());
        }
        User user = userValidation.getUser();



        Team team = teamRepository.findTeamById(sendInvitationRequest.getTeamId()).orElse(null);
        if(team == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not found");
        }

        User receiver = userRepository.findByUsername(sendInvitationRequest.getEmailReceiver()).orElse(null);
        if(receiver == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }



        try {
            Invitation invitation = new Invitation(receiver, team);
            invitationRepository.save(invitation);
            return ResponseEntity.status(HttpStatus.OK).body("Invitation sent");
        } catch (Exception e) {
            logger.error("Error while sending invitation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while sending invitation");
        }

    }

    public ResponseEntity<?> viewInvitationsForUser(HttpServletRequest request){
        UserValidationResponse userValidation = userComponent.getUserFromRequest(request);
        if(!userValidation.getStatus().equals("OK")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userValidation.getStatus());
        }
        User receiver = userValidation.getUser();

        try{
            List<ViewInvitationResponse> responses = invitationRepository.findAllByReceiver(receiver).stream().map(invitation -> {
                Team team = invitation.getTeam();
                return new ViewInvitationResponse(team.getId(), invitation.getId(), team.getName(), invitation.isRefused());
            }).toList();

            return ResponseEntity.status(HttpStatus.OK).body(responses);

        }catch (Exception e){
            logger.error("Error while fetching invitations", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while fetching invitations");
        }
    }

    public ResponseEntity<?> refuseInvitation(Long invId, HttpServletRequest request){
        UserValidationResponse userValidation = userComponent.getUserFromRequest(request);
        if(!userValidation.getStatus().equals("OK")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userValidation.getStatus());
        }
        User user = userValidation.getUser();


        Invitation invitation = invitationRepository.findById(invId).orElse(null);
        if(invitation == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invitation not found");
        }
        try {
            invitation.setRefused(true);
            invitationRepository.save(invitation);

            return ResponseEntity.status(HttpStatus.OK).body("Invitation refused");
        } catch (Exception e) {
            logger.error("Error while refusing invitation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while refusing invitation");
        }
    }

    @Transactional
    public ResponseEntity<?> acceptInvitation(Long invId, HttpServletRequest request){
        UserValidationResponse userValidation = userComponent.getUserFromRequest(request);
        if(!userValidation.getStatus().equals("OK")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userValidation.getStatus());
        }
        User receiver = userValidation.getUser();

        Invitation invitation = invitationRepository.findById(invId).orElse(null);
        if(invitation == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invitation not found");
        }
        Team team = invitation.getTeam();
        if(team == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not found");
        }
        if(invitation.isRefused()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invitation already refused");
        }

        receiver.getTeams().add(team);
        team.addUser(receiver);
        teamRepository.save(team);
        invitationRepository.delete(invitation);
        logger.info("New team: {}", team.getUsers());
        return ResponseEntity.status(HttpStatus.OK).body("Invitation accepted");
    }













}
