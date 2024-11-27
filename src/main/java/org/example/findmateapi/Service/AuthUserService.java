package org.example.findmateapi.Service;

import jakarta.servlet.http.HttpServletRequest;
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
import org.example.findmateapi.Service.Google.GmailAuth;
import org.example.findmateapi.Validation.EmailValidation;
import org.example.findmateapi.Validation.PasswordValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
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

    Logger logger = LoggerFactory.getLogger(AuthUserService.class);

    /**
     * If validations are successful, register user and generates confirmation code and sends email. Account is not active
     * until user confirms operation!
     * @param registerRequest RegisterRequest object
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
        user.setConfirmationCode(generateConfirmationCode());
        userRepository.save(user);
        sendConfirmationCodeMail(user.getEmail(), user);
        return ResponseEntity.ok("User registered successfully");
    }

    /**
     * Logs in user, generates JWT token
     * @return ResponseEntity with JWT token
     */
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
     * Confirms operation, user must be logged in
     * @param confirmationCode 6 char code as String
     * @param request Auth request
     * @return ResponseEntity with message
     */
    public ResponseEntity<?> confirmOperationAfterLogin(String confirmationCode, HttpServletRequest request){
        String token = JwtUtils.getJwtFromRequest(request);
        if(token == null || !jwtUtils.validateToken(token)){
            return ResponseEntity.badRequest().body("Unauthorized");
        }

        User user = getUserFromToken(token);
        if(user == null){
            return ResponseEntity.badRequest().body("User not found");
        }
        if(user.getConfirmationCode().equals(confirmationCode)){
            user.setConfirmationCode(null);
            user.setActive(true);
            userRepository.save(user);
            return ResponseEntity.ok("User activated successfully");
        }else{
            return ResponseEntity.badRequest().body("Invalid confirmation code");
        }
    }

//    public ResponseEntity<?> confirmOperationByEmail(String email, String confirmationCode){
//        Optional<User> userOptional = userRepository.findByEmail(email);
//        if(userOptional.isEmpty()){
//            return ResponseEntity.badRequest().body("User not found");
//        }
//        User user = userOptional.get();
//        if(user.getConfirmationCode().equals(confirmationCode)){
//            user.setConfirmationCode(null);
//            userRepository.save(user);
//            return ResponseEntity.ok("Operation confirmed successfully");
//        }
//        return ResponseEntity.badRequest().body("Invalid confirmation code");
//    }

    private User getUserFromToken(String token){
        String username = jwtUtils.extractUsername(token);
        return userRepository.findByUsername(username)
                .orElse(null);
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

    private String generateConfirmationCode(){
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        char[] confirmationCode = new char[6];
        for(int i = 0; i < confirmationCode.length; i++){
            confirmationCode[i] = base.charAt(random.nextInt(base.length()));
        }

        return new String(confirmationCode);
    }

    private void sendConfirmationCodeMail(String email, User user){
        try {
            GmailAuth.sendEmail(email, "[FindMate] Confirmation code",
                    "Your confirmation code is: " + user.getConfirmationCode() + "\n" +
                            "Enter this code to confirm your operation in FindMate");

        }catch (Exception e){
            logger.error("Error sending confirmation code email: {}", e.getMessage());
        }
    }

}
