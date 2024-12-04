package org.example.findmateapi.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.findmateapi.Entity.Invitation;
import org.example.findmateapi.Entity.Team;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ViewInvitationResponse {
    private Long teamId;
    private Long invitationId;
    private String teamName;
    private Boolean refused;


}
