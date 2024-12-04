package org.example.findmateapi.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.example.findmateapi.Entity.Team;
import org.example.findmateapi.Repository.TeamRepository;
import org.example.findmateapi.Request.CreateTeamRequest;
import org.example.findmateapi.Service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team")
@CrossOrigin(origins = "http://localhost:63342")
public class TeamController {

    @Autowired
    private TeamService teamService;
    @Autowired
    private TeamRepository teamRepository;

    //TODO: TEST

    @Operation(summary = "Create team", description = "Create team with given data, first user in list is a team leader (from token)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Team created successfully"),
            @ApiResponse(responseCode = "400", description = "Team could not be created")
    })
    @PostMapping("/create")
    private ResponseEntity<?> createTeam(@RequestBody CreateTeamRequest createTeamRequest, HttpServletRequest request){
        return teamService.createTeam(createTeamRequest, request);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTeams() {
        List<Team> teams = teamRepository.findAll();
        return ResponseEntity.ok(teams);
    }


}

