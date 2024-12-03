package org.example.findmateapi.Service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.findmateapi.Component.UserComponent;
import org.example.findmateapi.Entity.Cs2Profile;
import org.example.findmateapi.Entity.User;
import org.example.findmateapi.Entity.UserProfiles;
import org.example.findmateapi.Repository.Cs2ProfileRepository;
import org.example.findmateapi.Repository.UserProfilesRepository;
import org.example.findmateapi.Repository.UserRepository;
import org.example.findmateapi.Request.CreateCs2ProfileRequest;
import org.example.findmateapi.Request.FilterCs2ProfilesRequest;
import org.example.findmateapi.Request.UpdateCs2ProfileRequest;
import org.example.findmateapi.Response.ProfilesFullResponse;
import org.example.findmateapi.Response.ProfilesResponse;
import org.example.findmateapi.Security.Jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Service
public class Cs2ProfileService {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProfilesRepository userProfilesRepository;
    @Autowired
    private Cs2ProfileRepository cs2ProfileRepository;
    @Autowired
    private UserComponent userComponent;


    Logger logger = LoggerFactory.getLogger(Cs2ProfileService.class);

    public ResponseEntity<?> createCs2Profile(CreateCs2ProfileRequest createCs2ProfileRequest, HttpServletRequest request) {

        HashMap<String, User> response = userComponent.getUserFromRequest(request);
        if(!response.containsKey("OK")){
            String error = response.keySet().stream().findFirst().orElse("Unknown error");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        User user = response.get("OK");


        try{
            UserProfiles userProfiles = userProfilesRepository.findByUser(user).orElse(null);
            if (userProfiles == null) {
                createUserProfiles(user);
                userProfiles = user.getUserProfiles();
            }
            if(userProfiles.getCs2Profile() != null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already has a Cs2 Profile");
            }

            Cs2Profile cs2Profile = new Cs2Profile(userProfiles, createCs2ProfileRequest.getPrimeRank());
            cs2ProfileRepository.save(cs2Profile);

            userProfiles.setCs2Profile(cs2Profile);
            userProfilesRepository.save(userProfiles);
            return ResponseEntity.status(HttpStatus.CREATED).body("Cs2 Profile created successfully");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating Cs2 Profile");
        }


    }

    public ResponseEntity<?> refreshCs2Profile(HttpServletRequest request){
        HashMap<String, User> response = userComponent.getUserFromRequest(request);
        if(!response.containsKey("OK")){
            String error = response.keySet().stream().findFirst().orElse("Unknown error");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        User user = response.get("OK");

        Cs2Profile cs2Profile = cs2ProfileRepository.findByUserProfiles(user.getUserProfiles()).orElse(null);
        if(cs2Profile == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find Cs2 Profile");
        }else{
            cs2Profile.setLastRefreshed(LocalDateTime.now());
            cs2ProfileRepository.save(cs2Profile);
            return ResponseEntity.ok("Cs2 Profile refreshed successfully");
        }
    }

    public ResponseEntity<?> searchCs2Profiles(FilterCs2ProfilesRequest filterCs2ProfilesRequest, HttpServletRequest request){
        HashMap<String, User> responseUser = userComponent.getUserFromRequest(request);
        if(!responseUser.containsKey("OK")){
            String error = responseUser.keySet().stream().findFirst().orElse("Unknown error");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        User user = responseUser.get("OK");

        List<Cs2Profile> cs2Profiles = cs2ProfileRepository.filterCs2Profiles(filterCs2ProfilesRequest);
        if(cs2Profiles == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error filtering Cs2 Profiles");
        }

        try{
            if(filterCs2ProfilesRequest.getFullInfo()){
                List<ProfilesFullResponse> response = createFullResponse(cs2Profiles);
                if(response == null){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating response");
                }
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            List<ProfilesResponse> response = createResponse(cs2Profiles);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e){
            logger.error("Error:    CAN NOT CREATE RESPONSE       {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating response");
        }


    }

    public ResponseEntity<?> updateCs2Profile(UpdateCs2ProfileRequest updateRequest, HttpServletRequest request){
        HashMap<String, User> response = userComponent.getUserFromRequest(request);
        if(!response.containsKey("OK")){
            String error = response.keySet().stream().findFirst().orElse("Unknown error");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        User user = response.get("OK");

        Cs2Profile cs2Profile = findCs2ProfileByUser(user);
        if(cs2Profile == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find Cs2 Profile");
        }

        try {
            cs2Profile.setPrimeRank(updateRequest.getPrimeRank());
            cs2ProfileRepository.save(cs2Profile);
            return ResponseEntity.status(HttpStatus.OK).body("Cs2 Profile updated successfully");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating Cs2 Profile");
        }
    }



    protected Cs2Profile findCs2ProfileByUser(User user){
        UserProfiles userProfiles = userProfilesRepository.findByUser(user).orElse(null);
        if(userProfiles == null){
            return null;
        }
        return cs2ProfileRepository.findByUserProfiles(userProfiles).orElse(null);

    }

    private void createUserProfiles(User user){
        UserProfiles userProfiles = new UserProfiles();
        userProfiles.setUser(user);
        user.setUserProfiles(userProfiles);
        userRepository.save(user);
    }

    private List<ProfilesResponse> createResponse(List<Cs2Profile> profilescs2){
        List<ProfilesResponse> response = new ArrayList<>();

        for(int i = 0; i < profilescs2.size(); i++){
            Cs2Profile tempcs2 = profilescs2.get(i);

            UserProfiles userProfiles = userProfilesRepository.findByCs2Profile(tempcs2).orElse(null);
            if(userProfiles == null){
                return null;
            }

            User user = userRepository.findUserByUserProfiles(userProfiles).orElse(null);
            if(user == null){
                return null;
            }

            response.add(new ProfilesResponse(user.getId(), user.getUsername(), user.getEmail(), tempcs2));

        }

        return response;
    }

    private List<ProfilesFullResponse> createFullResponse(List<Cs2Profile> profilescs2){
        List<ProfilesFullResponse> response = new ArrayList<>();

        for(int i = 0; i < profilescs2.size(); i++){
            Cs2Profile tempcs2 = profilescs2.get(i);

            UserProfiles userProfiles = userProfilesRepository.findByCs2Profile(tempcs2).orElse(null);
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



}
