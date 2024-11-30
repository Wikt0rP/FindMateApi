package org.example.findmateapi.Service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.findmateapi.Entity.Cs2Profile;
import org.example.findmateapi.Entity.User;
import org.example.findmateapi.Entity.UserProfiles;
import org.example.findmateapi.Repository.Cs2ProfileRepository;
import org.example.findmateapi.Repository.Cs2ProfileRepositoryCustom;
import org.example.findmateapi.Repository.UserProfilesRepository;
import org.example.findmateapi.Repository.UserRepository;
import org.example.findmateapi.Request.CreateCs2ProfileRequest;
import org.example.findmateapi.Request.FilterCs2ProfilesRequest;
import org.example.findmateapi.Security.Jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static org.example.findmateapi.Component.UserComponent.getUserFromToken;

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


    Logger logger = LoggerFactory.getLogger(Cs2ProfileService.class);

    public ResponseEntity<?> createCs2Profile(CreateCs2ProfileRequest createCs2ProfileRequest, HttpServletRequest request) {

        String token = JwtUtils.getJwtFromRequest(request);
        if(token == null || !jwtUtils.validateToken(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        User user = getUserFromToken(token);
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find user");
        }


        UserProfiles userProfiles = userProfilesRepository.findByUser(user).orElse(null);
        if(userProfiles == null){
            createUserProfiles(user);
        }

        try{
            Cs2Profile cs2Profile = new Cs2Profile(userProfiles, createCs2ProfileRequest.getPrimeRank());
            if(userProfiles == null){
                logger.error("Error:    YOU FUCKED UP       {}", "UserProfiles is null");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Can not find UserProfiles");
            }
            userProfiles.setCs2Profile(cs2Profile);

            return ResponseEntity.status(HttpStatus.CREATED).body("Cs2 Profile created successfully");
        }catch (Exception e){
            logger.error("Error:    CAN NOT CREATE CS2 PROFILE       {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating Cs2 Profile");
        }


    }

    public ResponseEntity<?> refreshCs2Profile(HttpServletRequest request){
        String token = JwtUtils.getJwtFromRequest(request);
        if(token == null || !jwtUtils.validateToken(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        User user = getUserFromToken(token);
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find user");
        }

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
        String token = JwtUtils.getJwtFromRequest(request);
        if(token == null || !jwtUtils.validateToken(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        User user = getUserFromToken(token);
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find user");
        }
        List<Cs2Profile> cs2Profiles = cs2ProfileRepository.filterCs2Profiles(filterCs2ProfilesRequest);
        if(cs2Profiles != null){
            return ResponseEntity.status(HttpStatus.OK).body(cs2Profiles);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error filtering Cs2 Profiles");
    }

    private void createUserProfiles(User user){
        UserProfiles userProfiles = new UserProfiles();
        userProfiles.setUser(user);
        user.setUserProfiles(userProfiles);
        userRepository.save(user);
    }



}
