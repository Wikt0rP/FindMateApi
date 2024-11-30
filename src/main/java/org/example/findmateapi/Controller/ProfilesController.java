package org.example.findmateapi.Controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.example.findmateapi.Request.CreateCs2ProfileRequest;
import org.example.findmateapi.Service.Cs2ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "http://localhost:63342")
public class ProfilesController {

    @Autowired
    private Cs2ProfileService cs2ProfileService;

    @PostMapping("/cs2Create")
    @Operation(summary = "Create Cs2 Profile", description = "Create Cs2 Profile with given data, requires token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cs2 Profile created successfully"),
            @ApiResponse(responseCode = "404", description = "Team could not be created, UserProfiles not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Can not find UserProfiles and can not create UserProfiles")
    })
    public ResponseEntity<?> createCs2Profile(@RequestBody CreateCs2ProfileRequest createCs2ProfileRequest, HttpServletRequest request){
        return cs2ProfileService.createCs2Profile(createCs2ProfileRequest, request);
    }

}
