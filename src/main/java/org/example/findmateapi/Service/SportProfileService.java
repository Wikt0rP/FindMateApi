package org.example.findmateapi.Service;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.servlet.http.HttpServletRequest;
import org.example.findmateapi.Component.UserComponent;
import org.example.findmateapi.Entity.*;
import org.example.findmateapi.Repository.SportProfileRepository;
import org.example.findmateapi.Repository.UserProfilesRepository;
import org.example.findmateapi.Repository.UserRepository;
import org.example.findmateapi.Request.CreateSportProfileRequest;
import org.example.findmateapi.Request.FilterSportProfilesRequest;
import org.example.findmateapi.Response.ProfileSportResponse;
import org.example.findmateapi.Response.ProfilesFullResponse;
import org.example.findmateapi.Response.UserValidationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SportProfileService {
    @Autowired
    private  SportProfileRepository sportProfileRepository;
    @Autowired
    private  UserProfilesRepository userProfilesRepository;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  UserComponent userComponent;
    @Autowired
    private  CityService cityService;


    private final Logger logger = LoggerFactory.getLogger(SportProfileService.class);

    public ResponseEntity<?> createSportProfile(CreateSportProfileRequest createSportProfileRequest, HttpServletRequest request) {
        UserValidationResponse userValidation = userComponent.getUserFromRequest(request);
        if (!userValidation.getStatus().equals("OK")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userValidation.getStatus());
        }
        User user = userValidation.getUser();

        try {
            UserProfiles userProfiles = userProfilesRepository.findByUser(user).orElse(null);
            if (userProfiles == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User profiles not found");
            }

            SportProfile sportProfile = new SportProfile(userProfiles, createSportProfileRequest);
            sportProfileRepository.save(sportProfile);
            userProfiles.setSportProfile(sportProfile);
            userProfilesRepository.save(userProfiles);
            return ResponseEntity.ok("Sport profile created successfully");
        } catch (Exception e) {
            logger.error("Error creating sport profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sport profile could not be created");
        }
    }
    public ResponseEntity<?> refreshSportProfile(HttpServletRequest request) {
        UserValidationResponse userValidation = userComponent.getUserFromRequest(request);
        if (!userValidation.getStatus().equals("OK")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userValidation.getStatus());
        }
        User user = userValidation.getUser();

        SportProfile sportProfile = findSportProfileByUser(user);
        if (sportProfile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sport profile not found");
        }
        try {
            sportProfile.setLastRefreshTime(LocalDateTime.now());
            sportProfileRepository.save(sportProfile);
            return ResponseEntity.ok("Sport profile refreshed successfully");
        } catch (Exception e) {
            logger.error("Error refreshing sport profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sport profile could not be refreshed");
        }
    }


    public ResponseEntity<?> searchSportProfiles(FilterSportProfilesRequest filterSportProfilesRequest, HttpServletRequest request) {
        UserValidationResponse userValidation = userComponent.getUserFromRequest(request);
        if (!userValidation.getStatus().equals("OK")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userValidation.getStatus());
        }
        User user = userValidation.getUser();

        City city = cityService.getCityByName(filterSportProfilesRequest.getBaseCity());
        if (city == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("City not found");
        }

        List<SportProfile> matchedProfiles = sportProfileRepository.filterSportProfiles(filterSportProfilesRequest);

        if (matchedProfiles.isEmpty()) {
            return ResponseEntity.ok("No matching profiles found");
        }

        if (Boolean.TRUE.equals(filterSportProfilesRequest.getFullInfo())) {
            List<ProfilesFullResponse> response = createFullResponse(matchedProfiles);
            return ResponseEntity.ok(response);
        } else {
            List<ProfileSportResponse> response = createPartialResponse(matchedProfiles);
            return ResponseEntity.ok(response);
        }
    }

    private List<ProfilesFullResponse> createFullResponse(List<SportProfile> matchedProfiles) {
        return matchedProfiles.stream()
                .map(profile -> new ProfilesFullResponse(profile.getId(), profile.getUserProfiles().getUser().getUsername(), profile.getUserProfiles().getUser().getEmail(), profile.getUserProfiles()))
                .collect(Collectors.toList());


        
    }


    private List<ProfileSportResponse> createPartialResponse(List<SportProfile> matchedProfiles) {
        return matchedProfiles.stream()
                .map(profile -> new ProfileSportResponse(profile.getId(), profile.getUserProfiles().getUser().getUsername(), profile.getSport(), profile.getCity()))
                .collect(Collectors.toList());
    }
    protected SportProfile findSportProfileByUser(User user) {
        UserProfiles userProfiles = userProfilesRepository.findByUser(user).orElse(null);
        if (userProfiles == null) {
            return null;
        }
        return sportProfileRepository.findSportProfileByUserProfiles(userProfiles)
                .orElse(null);
    }
}
