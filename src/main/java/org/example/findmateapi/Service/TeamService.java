package org.example.findmateapi.Service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.findmateapi.Component.UserComponent;
import org.example.findmateapi.Entity.Team;
import org.example.findmateapi.Entity.User;
import org.example.findmateapi.Repository.TeamRepository;
import org.example.findmateapi.Repository.UserRepository;
import org.example.findmateapi.Request.CreateTeamRequest;
import org.example.findmateapi.Response.UserValidationResponse;
import org.example.findmateapi.Security.Jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserComponent userComponent;

    private final Logger logger = LoggerFactory.getLogger(TeamService.class);

    public ResponseEntity<?> createTeam(CreateTeamRequest createTeamRequest, HttpServletRequest request) {
        UserValidationResponse userValidation = userComponent.getUserFromRequest(request);
        if(!userValidation.getStatus().equals("OK")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userValidation.getStatus());
        }
        User user = userValidation.getUser();

        try{
            Team team = new Team(createTeamRequest.getTeamName(), user);
            user.getTeams().add(team);
            teamRepository.save(team);
            userRepository.save(user);
            return ResponseEntity.ok("Team created successfully");
        }catch (Exception e){
            logger.error("Error creating team: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Team could not be created");
        }
    }

    public ResponseEntity<?> deleteUser(Long teamId, HttpServletRequest request){
        UserValidationResponse userValidation = userComponent.getUserFromRequest(request);
        if(!userValidation.getStatus().equals("OK")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userValidation.getStatus());
        }
        User user = userValidation.getUser();

        Optional<Team> teamOptional = teamRepository.findTeamById(teamId);

        if (!teamOptional.isPresent()) {
            throw new IllegalArgumentException("Zespół nie istnieje");
        }

        Team team = teamOptional.get();
        user.getTeams().remove(team);
        userRepository.save(user);

        return ResponseEntity.ok("działa");
    }



    public ResponseEntity<?> getTeamUsers(Long teamId){
        Team team = teamRepository.findTeamById(teamId)
                .orElse(null);
        if(team != null){
            return ResponseEntity.ok(team.getUsers());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not found");
    }

}
