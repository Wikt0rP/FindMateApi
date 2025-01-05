package org.example.findmateapi.Controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.example.findmateapi.Request.*;
import org.example.findmateapi.Service.Cs2ProfileService;
import org.example.findmateapi.Service.LolProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "http://localhost:63342")
public class ProfilesController {

    @Autowired
    private Cs2ProfileService cs2ProfileService;
    @Autowired
    private LolProfileService lolProfileService;


    /// Cs2 Profile
    @PostMapping("/createCs2")
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

    @PostMapping("/refreshDateCs2")
    @Operation(summary = "Refresh Cs2 Profile", description = "Refresh Cs2 Profile, requires token." +
            "This date is used to sort users when searching for teammastes (newest first)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cs2 Profile date refreshed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Can not find Cs2Profile or UserProfiles")
    })
    public ResponseEntity<?> refreshDateCs2(HttpServletRequest request){
        return cs2ProfileService.refreshCs2Profile(request);
    }

    @GetMapping("/searchCs2")
    @Operation(summary = "Search Cs2 Profiles", description = "Search Cs2 Profiles with filters<br>" +
            "Requires token<br>" +
            "If want to see all profiles (fullInfo = true) <br><br>" +
            "EXAMPLE: <br>127.0.0.1:8080/profile/searchCs2?minPrimeRank=INTEGER&maxPrimeRank=INTEGER&lastRefreshed=2024-11-10T22:00:00&fullInfo=True")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cs2 Profiles found successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Can not find UserProfiles"),
            @ApiResponse(responseCode = "500", description = "Can not filter Cs2Profiles")
    })
    public ResponseEntity<?> searchCs2Profiles(@RequestParam(required = false) Integer minPrimeRank, @RequestParam(required = false) Integer maxPrimeRank, @RequestParam(required = false) LocalDateTime lastRefreshed,
                                               @RequestParam(required = false) Boolean fullInfo, HttpServletRequest request){

        FilterCs2ProfilesRequest filterCs2ProfilesRequest = new FilterCs2ProfilesRequest(minPrimeRank, maxPrimeRank, lastRefreshed, fullInfo);
        return cs2ProfileService.searchCs2Profiles(filterCs2ProfilesRequest, request);
    }

    @PutMapping("/updateCs2")
    @Operation(summary = "Update Cs2 Profile", description = "Update Cs2 Profile with given data<br>" +
            " requires token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cs2 Profile updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Can not find Cs2Profile"),
            @ApiResponse(responseCode = "500", description = "Can not update Cs2Profile")
    })
    public ResponseEntity<?> updateCs2Profile(@RequestBody UpdateCs2ProfileRequest updateCs2ProfileRequest, HttpServletRequest request){
        return cs2ProfileService.updateCs2Profile(updateCs2ProfileRequest, request);
    }



    /// Lol Profile
    @PostMapping("/createLol")
    @Operation(summary = "Create LoL Profile", description = "Create LoL Profile with given data, requires token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "LoL Profile created successfully"),
            @ApiResponse(responseCode = "404", description = "Team could not be created, UserProfiles not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Can not find UserProfiles and can not create UserProfiles")
    })
    public ResponseEntity<?> createLolProfile(@RequestBody CreateLolProfileRequest createLolProfileRequest, HttpServletRequest request){
        return lolProfileService.createLolProfile(createLolProfileRequest, request);
    }

    @PostMapping("/searchLol")
    @Operation(summary = "Search Lol Profiles", description = "Search lol Profiles with filters<br>" +
            "Requires token<br>" +
            "If want to see all profiles (fullInfo = true) <br><br>"
            )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cs2 Profiles found successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Can not find UserProfiles"),
            @ApiResponse(responseCode = "500", description = "Can not filter LoLProfiles")
    })
    public ResponseEntity<?> searchLolProfiles(@RequestBody FilterLolProfilesRequest filterLolProfilesRequest, HttpServletRequest request){
        return lolProfileService.searchLolProfiles(filterLolProfilesRequest, request);
    }


    @PutMapping("/updatelol")
    @Operation(summary = "Update LoL Profile", description = "Update LoL Profile with given data<br>" +
            " requires token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "LoL Profile updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Can not find LoLProfile"),
            @ApiResponse(responseCode = "500", description = "Can not update LoLProfile")
    })
    public ResponseEntity<?> updateLolProfile(@RequestBody UpdateLolProfileRequest updateLolProfileRequest, HttpServletRequest request){
        return lolProfileService.updateLolProfile(updateLolProfileRequest, request);
    }

    @PostMapping("/refreshDateLol")
    @Operation(summary = "Refresh LoL Profile", description = "Refresh LoL Profile, requires token." +
            "This date is used to sort users when searching for teammastes (newest first)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "LoL Profile date refreshed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Can not find LoLProfile or UserProfiles")
    })
    public ResponseEntity<?> refreshDateLol(HttpServletRequest request){
        return lolProfileService.refreshLolProfile(request);
    }
}
