package org.example.findmateapi.Controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.example.findmateapi.Request.LoginRequest;
import org.example.findmateapi.Request.RegisterRequest;
import org.example.findmateapi.Request.ResetPasswordRequest;
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

    @Operation(summary = "Register new user", description = "Register new user with given data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "User could not be registered")
    })
    @PostMapping("/register")
    public ResponseEntity<?> userRegister(@RequestBody RegisterRequest registerRequest){
        return authUserService.registerUser(registerRequest);
    }

    @Operation(summary = "Login user", description = "Login user with given data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User logged in successfully, get token"),
            @ApiResponse(responseCode = "400", description = "Bad credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequest loginRequest){
        return authUserService.loginUser(loginRequest);
    }

    @Operation(summary = "Activate user", description = "Activate user with given code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User activated successfully"),
            @ApiResponse(responseCode = "400", description = "User could not be activated, try generating new code")
    })
    @PostMapping("/activate")
    public ResponseEntity<?> activateUser(@RequestBody String code, HttpServletRequest request){
        return authUserService.confirmOperationAfterLogin(code, request);
    }

    //TODO: TEST
    @Operation(summary="Forgot password", description="Send email to user with reset code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email sent successfully"),
            @ApiResponse(responseCode = "400", description = "Email could not be sent")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody String email){
        return authUserService.forgotPassword(email);
    }

    //TODO: TEST
    @Operation(summary="Change password", description="Change password with given reset code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Password could not be changed")
    })
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
        return authUserService.changePassword(resetPasswordRequest);
    }
}
