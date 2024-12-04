package org.example.findmateapi.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.findmateapi.Entity.Team;
import org.example.findmateapi.Entity.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendInvitationRequest {
    private String emailReceiver;
    private Long teamId;

}
