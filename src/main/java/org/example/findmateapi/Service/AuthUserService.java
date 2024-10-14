package org.example.findmateapi.Service;

import org.example.findmateapi.Entity.User;
import org.example.findmateapi.Repository.UserRepository;
import org.example.findmateapi.Request.RegisterRequest;
import org.example.findmateapi.Security.Config.PasswordEncoderConfig;
import org.example.findmateapi.Security.Jwt.JwtUtils;
import org.example.findmateapi.Validation.EmailValidation;
import org.example.findmateapi.Validation.PasswordValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailValidation emailValidation;
    @Autowired
    private PasswordValidation passwordValidation;
    @Autowired
    private PasswordEncoderConfig passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * If validations are successful, register user.
     * @return ResponseEntity with message
     */
    public ResponseEntity<?> registerUser(RegisterRequest registerRequest){
        if(registerValidations(registerRequest).getStatusCode().is4xxClientError()){
            return registerValidations(registerRequest);
        }
        if(userRepository.existsByUsername(registerRequest.getUsername())){
            return ResponseEntity.badRequest().body("Username already exists");
        }

        User user = new User(registerRequest.getUsername(), registerRequest.getEmail(),
                passwordEncoder.passwordEncoder().encode(registerRequest.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    /**
     *  Validates email, password, username and checks if username already exists
     *  @return ResponseEntity with message
     */
    private ResponseEntity<String> registerValidations(RegisterRequest registerRequest){
        if(!emailValidation.isValidEmail(registerRequest.getEmail())){
            return ResponseEntity.badRequest().body("Email validation unsuccessful");
        }
        if(!passwordValidation.isValidPassword(registerRequest.getPassword(), registerRequest.getUsername(), 3)){
            return ResponseEntity.badRequest().body("Password validation unsuccessful");
        }
        if(userRepository.existsByUsername(registerRequest.getUsername())){
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if(registerRequest.getUsername().length() < 3){
            return ResponseEntity.badRequest().body("Username must be at least 3 characters long");
        }
        if(userRepository.existsByUsername(registerRequest.getUsername())){
            return ResponseEntity.badRequest().body("Username already exists");
        }
        return ResponseEntity.ok("Validations successful");
    }
}
