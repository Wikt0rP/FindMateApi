package org.example.findmateapi.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.findmateapi.Request.LoginRequest;
import org.example.findmateapi.Request.RegisterRequest;
import org.example.findmateapi.Service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:63342")
public class AuthControler {
    @Autowired
    private AuthUserService authUserService;

    @PostMapping("/register")
    public ResponseEntity<?> userRegister(@RequestBody RegisterRequest registerRequest){
        return authUserService.registerUser(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequest loginRequest){
        return authUserService.loginUser(loginRequest);
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activateUser(@RequestBody String code, HttpServletRequest request){
        return authUserService.confirmOperationAfterLogin(code, request);
    }
}
