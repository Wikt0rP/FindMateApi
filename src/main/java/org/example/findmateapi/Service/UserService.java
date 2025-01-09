package org.example.findmateapi.Service;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.example.findmateapi.Component.UserComponent;
import org.example.findmateapi.Entity.LolProfile;
import org.example.findmateapi.Entity.User;
import org.example.findmateapi.Response.UserValidationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    public UserComponent userComponent;

    @Autowired
    public LolProfileService lolProfileService;

    @Autowired
    public Cs2ProfileService cs2ProfileService;

    record UserInfo(String username, String email){}

    public ResponseEntity<?> userInfo(HttpServletRequest request){
        UserValidationResponse userValidation = userComponent.getUserFromRequest(request);
        if(!userValidation.getStatus().equals("OK")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userValidation.getStatus());
        }
        User user = userValidation.getUser();
        UserInfo info = new UserInfo(user.getUsername(), user.getEmail());
        return ResponseEntity.ok().body(info);
    }

    public ResponseEntity<?> userLolProfile(HttpServletRequest request){
        UserValidationResponse userValidation = userComponent.getUserFromRequest(request);
        if(!userValidation.getStatus().equals("OK")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userValidation.getStatus());
        }
        User user = userValidation.getUser();
        LolProfile lolProfile = lolProfileService.findLolProfileByUser(user);
        return ResponseEntity.ok().body(lolProfile);
    }

    public ResponseEntity<?> userCsProfile(HttpServletRequest request){
        UserValidationResponse userValidation = userComponent.getUserFromRequest(request);
        if(!userValidation.getStatus().equals("OK")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userValidation.getStatus());
        }
        User user = userValidation.getUser();
        return ResponseEntity.ok().body(cs2ProfileService.findCs2ProfileByUser(user));
    }
}
