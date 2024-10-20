package org.example.findmateapi.Service;

import org.apache.coyote.Response;
import org.example.findmateapi.Entity.ERole;
import org.example.findmateapi.Entity.Role;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthUserServiceTest {
    @InjectMocks
    private AuthUserService authUserService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailValidation emailValidation;
    @Mock
    private PasswordValidation passwordValidation;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private PasswordEncoderConfig passwordEncoderConfig;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private Authentication authentication;
    @Mock
    private UserDetailsImpl userDetails;
    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        when(passwordEncoderConfig.passwordEncoder()).thenReturn(passwordEncoder);
    }

    @Test
    void succesfulSignUpTest(){
        String username = "test";
        String email = "email@gmail.com";
        String password = "Password123@";
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);

        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(false);
        when(emailValidation.isValidEmail(registerRequest.getEmail())).thenReturn(true);
        when(passwordValidation.isValidPassword(registerRequest.getPassword(), registerRequest.getUsername(), 3 )).thenReturn(true);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(new Role(ERole.ROLE_USER)));
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        ResponseEntity<?> response = authUserService.registerUser(registerRequest);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void failedSignupTestUsernameIsTaken(){
        String username = "username";
        String email = "mail@gmail.com";
        String password = "Password123@";
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);

        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(true);
        when(emailValidation.isValidEmail(registerRequest.getEmail())).thenReturn(true);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(new Role(ERole.ROLE_USER)));
        when(passwordValidation.isValidPassword(registerRequest.getPassword(), username, 3)).thenReturn(true);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        ResponseEntity<?> response = authUserService.registerUser(registerRequest);
        assertTrue(response.getStatusCode().is4xxClientError(), "Register is not successful, username is taken.");

    }

    @Test
    void failedSignupTestEmailIsTaken(){
        String username = "username";
        String email = "mail@gmail.com";
        String password = "Password123@";
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);

        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(true);
        when(emailValidation.isValidEmail(registerRequest.getEmail())).thenReturn(true);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(new Role(ERole.ROLE_USER)));
        when(passwordValidation.isValidPassword(registerRequest.getPassword(), username, 3)).thenReturn(true);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        ResponseEntity<?> response = authUserService.registerUser(registerRequest);
        assertTrue(response.getStatusCode().is4xxClientError(), "Register is not successful, username is taken.");

    }

    @Test
    void failedSignupTestEmailIsInvalid() {
        String username = "username";
        String email = "email@gm";
        String password = "Password123@";
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);

        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(false);
        when(emailValidation.isValidEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordValidation.isValidPassword(registerRequest.getPassword(), username, 3)).thenReturn(true);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(new Role(ERole.ROLE_USER)));
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        ResponseEntity<?> response = authUserService.registerUser(registerRequest);
        assertTrue(response.getStatusCode().is4xxClientError(), "Register is not successful, email is invalid.");
    }

    @Test
    void failedSignupTestPasswordIsInvalid() {
        String username = "username";
        String email = "mail@gmail.com";
        String password = "error";
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);

        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(false);
        when(emailValidation.isValidEmail(registerRequest.getEmail())).thenReturn(true);
        when(passwordValidation.isValidPassword(registerRequest.getPassword(), username, 3)).thenReturn(false);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(new Role(ERole.ROLE_USER)));
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        ResponseEntity<?> response = authUserService.registerUser(registerRequest);
        assertTrue(response.getStatusCode().is4xxClientError(), "Register is not successful, password is invalid.");
    }

    @Test
    void successfulSignInTest() {
        String username = "username";
        String password = "Password123@";
        LoginRequest authRequest = new LoginRequest(username, password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);
        when(userDetails.getId()).thenReturn(1L);
        when(userDetails.getEmail()).thenReturn("expected@mail.com");
        when(userDetails.getAuthorities()).thenReturn(new ArrayList<>());

        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwtToken");

        ResponseEntity<?> response = authUserService.loginUser(authRequest);

        assertTrue(response.getStatusCode().is2xxSuccessful(), "Respond should be successful");
        assertEquals("jwtToken", ((JwtResponse) Objects.requireNonNull(response.getBody())).getToken(), "Token should be jwtToken");
    }

    @Test
    void failedSignInTest() {
        String username = "username";
        String password = "Password123@";
        LoginRequest authRequest = new LoginRequest(username, password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        ResponseEntity<?> response = authUserService.loginUser(authRequest);

        assertTrue(response.getStatusCode().is4xxClientError(), "Respond should be unsuccessful");
    }

}
