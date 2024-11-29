package org.example.findmateapi.Service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.findmateapi.Entity.Team;
import org.example.findmateapi.Entity.User;
import org.example.findmateapi.Repository.TeamRepository;
import org.example.findmateapi.Repository.UserRepository;
import org.example.findmateapi.Request.CreateTeamRequest;
import org.example.findmateapi.Security.Jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> createTeam(CreateTeamRequest createTeamRequest, HttpServletRequest request) {

        try{
            String token = JwtUtils.getJwtFromRequest(request);
            if(token == null || !jwtUtils.validateToken(token)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }

            User user = getUserFromToken(token);
            if(user == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            if(createTeamRequest.getTeamName() == null || createTeamRequest.getTeamName().isEmpty()){
                createTeamRequest.setTeamName(user.getUsername()+"'s team");
            }

            Team team = new Team(createTeamRequest.getTeamName(), user);
            teamRepository.save(team);
            return ResponseEntity.ok("Team created successfully");

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Team could not be created");
        }

    }

    private User getUserFromToken(String token){
        String username = jwtUtils.extractUsername(token);
        if (username == null || username.isEmpty()) {
            return null;
        }
        return userRepository.findByUsername(username).orElse(null);
    }
}
