package org.example.findmateapi.Service;

import org.example.findmateapi.Entity.ERole;
import org.example.findmateapi.Entity.Role;
import org.example.findmateapi.Entity.User;
import org.example.findmateapi.Repository.RoleRepository;
import org.example.findmateapi.Repository.UserRepository;
import org.example.findmateapi.Request.LoginRequest;
import org.example.findmateapi.Request.RegisterRequest;
import org.example.findmateapi.Response.JwtResponse;
import org.example.findmateapi.Security.Config.PasswordEncoderConfig;
import org.example.findmateapi.Security.Impl.UserDetailsImpl;
import org.example.findmateapi.Security.Jwt.JwtUtils;
import org.example.findmateapi.Validation.EmailValidation;
import org.example.findmateapi.Validation.PasswordValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Autowired
    private RoleRepository roleRepository;

    /**
     * If validations are successful, register user.
     * @return ResponseEntity with message
     */
    public ResponseEntity<?> registerUser(RegisterRequest registerRequest){

        ResponseEntity<?> validationResponse = registerValidations(registerRequest);
        if(validationResponse.getStatusCode().is4xxClientError()){
            return validationResponse;
        }
        if(userRepository.existsByUsername(registerRequest.getUsername())){
            return ResponseEntity.badRequest().body("Username already exists");
        }

        Role role = roleRepository.findByName(ERole.ROLE_USER)
                .orElseGet(() -> roleRepository.save(new Role(ERole.ROLE_USER)));

        User user = new User(registerRequest.getUsername(), registerRequest.getEmail(),
        passwordEncoder.passwordEncoder().encode(registerRequest.getPassword()), role);
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    //TODO: tests
    public ResponseEntity<?> loginUser(LoginRequest loginRequest){
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Set<Role> roles = userDetails.getAuthorities().stream()
                    .map(authority -> {
                        String roleName = authority.getAuthority();
                        ERole role = ERole.valueOf(roleName);
                        return new Role(role);
                    }).collect(Collectors.toSet());
            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
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
