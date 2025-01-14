package org.example.findmateapi.Controller;

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
public class SportProfileController {

    @Autowired
    private SportProfileService sportProfileService;
    @Autowired
    private SportProfileRepository sportProfileRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createSportProfile(@RequestBody CreateSportProfileRequest createSportProfileRequest, HttpServletRequest request) {
        return sportProfileService.createSportProfile(createSportProfileRequest, request);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshSportProfile(HttpServletRequest request) {
        return sportProfileService.refreshSportProfile(request);
    }


    @PostMapping("/search")
    public ResponseEntity<?> searchSportProfiles(@RequestBody FilterSportProfilesRequest filterSportProfilesRequest, HttpServletRequest request) {
        return sportProfileService.searchSportProfiles(filterSportProfilesRequest, request);
    }
    
    @GetMapping("/all")
    public ResponseEntity<?> getAllProfiles() {
        List<SportProfile> profiles = sportProfileRepository.findAll();

        // Wrap in ResponseEntity with 200 OK status
        return ResponseEntity.ok(profiles);
    }
}
