package org.example.findmateapi.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.example.findmateapi.Entity.SportProfile;
import org.example.findmateapi.Repository.SportProfileRepository;
import org.example.findmateapi.Request.CreateSportProfileRequest;
import org.example.findmateapi.Request.FilterSportProfilesRequest;
import org.example.findmateapi.Service.SportProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sportprofile")
@CrossOrigin(origins = "http://localhost:63342")
@Tag(name = "SportProfileController", description = "Endpoints for managing sport profiles")
public class SportProfileController {

    @Autowired
    private SportProfileService sportProfileService;
    @Autowired
    private SportProfileRepository sportProfileRepository;

    @PostMapping("/create")
    @Operation(summary = "Create a Sport Profile", description = "Creates a new sport profile for a user")
    public ResponseEntity<?> createSportProfile(@RequestBody CreateSportProfileRequest createSportProfileRequest, HttpServletRequest request) {
        return sportProfileService.createSportProfile(createSportProfileRequest, request);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh a Sport Profile", description = "Refreshes the last refresh time of a user's sport profile")
    public ResponseEntity<?> refreshSportProfile(HttpServletRequest request) {
        return sportProfileService.refreshSportProfile(request);
    }

    @PostMapping("/search")
    @Operation(summary = "Search Sport Profiles", description = "Searches for sport profiles based on filters")
    public ResponseEntity<?> searchSportProfiles(@RequestBody FilterSportProfilesRequest filterSportProfilesRequest, HttpServletRequest request) {
        return sportProfileService.searchSportProfiles(filterSportProfilesRequest, request);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all Sport Profiles", description = "Retrieves all sport profiles")
    public ResponseEntity<?> getAllProfiles() {
        List<SportProfile> profiles = sportProfileRepository.findAll();
        return ResponseEntity.ok(profiles);
    }
}
