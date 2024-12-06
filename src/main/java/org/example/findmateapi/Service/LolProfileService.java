package org.example.findmateapi.Service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.findmateapi.Component.UserComponent;
import org.example.findmateapi.Entity.Cs2Profile;
import org.example.findmateapi.Entity.LolProfile;
import org.example.findmateapi.Entity.User;
import org.example.findmateapi.Entity.UserProfiles;
import org.example.findmateapi.Repository.LolProfileRepository;
import org.example.findmateapi.Repository.UserProfilesRepository;
import org.example.findmateapi.Repository.UserRepository;
import org.example.findmateapi.Request.CreateLolProfileRequest;
import org.example.findmateapi.Request.FilterLolProfilesRequest;
import org.example.findmateapi.Response.ProfilesCs2Response;
import org.example.findmateapi.Response.ProfilesFullResponse;
import org.example.findmateapi.Response.ProfilesLolResponse;
import org.example.findmateapi.Response.UserValidationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class LolProfileService {

    @Autowired
    private UserComponent userComponent;
    @Autowired
    private UserProfilesRepository userProfilesRepository;
    @Autowired
    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(LolProfileService.class);
    @Autowired
    private LolProfileRepository lolProfileRepository;

    public ResponseEntity<?> createLolProfile(CreateLolProfileRequest createLolProfileRequest, HttpServletRequest request){
        UserValidationResponse userValidation = userComponent.getUserFromRequest(request);
        if(!userValidation.getStatus().equals("OK")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userValidation.getStatus());
        }
        User user = userValidation.getUser();

        try{
            UserProfiles userProfiles = userProfilesRepository.findByUser(user).orElse(null);
            if (userProfiles == null) {
                userComponent.createUserProfiles(user);
                userProfiles = user.getUserProfiles();
            }
            if(userProfiles.getLolProfile() != null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lol profile already exists");
            }

            LolProfile lolProfile = createLolProfile(userProfiles, createLolProfileRequest);
            lolProfileRepository.save(lolProfile);
            userProfiles.setLolProfile(lolProfile);
            userProfilesRepository.save(userProfiles);
            return ResponseEntity.ok("Lol profile created successfully");

        }catch (Exception e){
            logger.error("Error creating lol profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lol profile could not be created");
        }
    }

    public ResponseEntity<?> refreshLolProfile(HttpServletRequest request){
        UserValidationResponse userValidation = userComponent.getUserFromRequest(request);
        if(!userValidation.getStatus().equals("OK")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userValidation.getStatus());
        }
        User user = userValidation.getUser();

        LolProfile lolProfile = findLolProfileByUser(user);
        if(lolProfile == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lol profile not found");
        }
        try{
            lolProfileRepository.save(lolProfile);
            return ResponseEntity.ok("Lol profile refreshed successfully");
        }catch (Exception e){
            logger.error("Error refreshing lol profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lol profile could not be refreshed");
        }
    }

    public ResponseEntity<?> searchLolProfiles(FilterLolProfilesRequest filterLolProfilesRequest, HttpServletRequest request){
        UserValidationResponse userValidation = userComponent.getUserFromRequest(request);
        if(!userValidation.getStatus().equals("OK")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userValidation.getStatus());
        }
        User user = userValidation.getUser();

        List<LolProfile> lolProfiles = lolProfileRepository.filterLolProfiles(filterLolProfilesRequest);
        if(lolProfiles == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error filtering Lol Profiles");
        }

        try {
            if(filterLolProfilesRequest.getFullInfo()){
                List<ProfilesFullResponse> response = createFullResponse(lolProfiles);
                if(response == null){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating full response");
                }
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            List<ProfilesLolResponse> response = createResponse(lolProfiles);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e){
            logger.error("Error creating response: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating response");
        }
    }



    protected LolProfile findLolProfileByUser(User user){
        UserProfiles userProfiles = userProfilesRepository.findByUser(user).orElse(null);
        if(userProfiles == null){
            return null;
        }
        return lolProfileRepository.findByUserProfiles(userProfiles).orElse(null);
    }
    protected LolProfile createLolProfile(UserProfiles userProfiles, CreateLolProfileRequest createLolProfileRequest){
        LolProfile lolProfile = new LolProfile(userProfiles, createLolProfileRequest.getRank());
        lolProfile.setSummonerName(createLolProfileRequest.getSummonerName());
        lolProfile.setRoleTop(createLolProfileRequest.getRoleTop());
        lolProfile.setRoleJungle(createLolProfileRequest.getRoleJungle());
        lolProfile.setRoleMid(createLolProfileRequest.getRoleMid());
        lolProfile.setRoleAdc(createLolProfileRequest.getRoleAdc());
        lolProfile.setRoleSupport(createLolProfileRequest.getRoleSupport());
        return lolProfile;
    }

    private List<ProfilesFullResponse> createFullResponse(List<LolProfile> lolProfiles){
        List<ProfilesFullResponse> response = new ArrayList<>();

        for(int i = 0; i < lolProfiles.size(); i++){
            LolProfile tempLol = lolProfiles.get(i);

            UserProfiles userProfiles = userProfilesRepository.findByLolProfile(tempLol).orElse(null);
            if(userProfiles == null){
                return null;
            }

            User user = userRepository.findUserByUserProfiles(userProfiles).orElse(null);
            if(user == null){
                return null;
            }

            response.add(new ProfilesFullResponse(user.getId(), user.getUsername(), user.getEmail(), userProfiles));

        }

        return response;
    }

    private List<ProfilesLolResponse> createResponse(List<LolProfile> lolProfiles){
        List<ProfilesLolResponse> response = new ArrayList<>();

        for(int i = 0; i < lolProfiles.size(); i++){
            LolProfile tempLol = lolProfiles.get(i);

            UserProfiles userProfiles = userProfilesRepository.findByLolProfile(tempLol).orElse(null);
            if(userProfiles == null){
                return null;
            }

            User user = userRepository.findUserByUserProfiles(userProfiles).orElse(null);
            if(user == null){
                return null;
            }

            response.add(new ProfilesLolResponse(user.getId(), user.getUsername(), user.getEmail(), tempLol));

        }

        return response;
    }
}
