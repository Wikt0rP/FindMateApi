package org.example.findmateapi.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.findmateapi.Service.LolProfileService;
import org.example.findmateapi.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:63342")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo( HttpServletRequest request){
        return userService.userInfo(request);
    }

    @GetMapping("/lolprofile")
    public ResponseEntity<?> getUserLolProfile(HttpServletRequest request){
        return userService.userLolProfile(request);
    }

    @GetMapping("/csprofile")
    public ResponseEntity<?> getUserCsProfile(HttpServletRequest request){
        return userService.userCsProfile(request);
    }

    @GetMapping("/sportprofile")
    public ResponseEntity<?> getUserSportProfile(HttpServletRequest request){
        return userService.userSportProfile(request);
    }
}
