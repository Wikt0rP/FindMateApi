package org.example.findmateapi.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.example.findmateapi.Request.SendInvitationRequest;
import org.example.findmateapi.Service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invite")
@CrossOrigin(origins = "http://localhost:63342")
public class InvitationController {

    @Autowired
    private InvitationService invitationService;


    @Operation(summary = "Send invitation", description = "Send invitation to user with given email <br>" +
            "Require token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invitation sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invitation could not be sent")
    })
    @PostMapping("/send")
    public ResponseEntity<?> sendInvitation(@RequestBody SendInvitationRequest sendInvitationRequest, HttpServletRequest request){
        return invitationService.createInvitation(sendInvitationRequest, request);
    }

    @Operation(summary = "View invitations", description = "View all invitations for user <br>" +
            "Require token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invitations viewed successfully"),
            @ApiResponse(responseCode = "400", description = "Invitations could not be viewed")
    })
    @GetMapping("/view")
    public ResponseEntity<?> viewInvitations(HttpServletRequest request){
        return invitationService.viewInvitationsForUser(request);
    }

    @Operation(summary = "Refuse invitation", description = "Refuse invitation with given id <br>" +
            "Require token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invitation refused successfully"),
            @ApiResponse(responseCode = "400", description = "Invitation could not be refused")
    })
    @PutMapping("/refuse")
    public ResponseEntity<?> refuseInvitation(@RequestParam Long invitationId, HttpServletRequest request){
        return invitationService.refuseInvitation(invitationId, request);
    }

    @Operation(summary = "Accept invitation", description = "Accept invitation with given id <br>" +
            "Require token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invitation accepted successfully"),
            @ApiResponse(responseCode = "400", description = "Invitation could not be accepted")
    })
    @PutMapping("/accept")
    public ResponseEntity<?> acceptInvitation(@RequestParam Long invitationId, HttpServletRequest request){
        return invitationService.acceptInvitation(invitationId, request);
    }

}
