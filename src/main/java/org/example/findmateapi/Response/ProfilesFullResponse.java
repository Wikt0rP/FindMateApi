package org.example.findmateapi.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.findmateapi.Entity.Cs2Profile;
import org.example.findmateapi.Entity.UserProfiles;

//@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfilesFullResponse {

    private Long userId;
    private String username;
    private String email;
    private UserProfiles userProfiles;


    public ProfilesFullResponse(Long userId, String username, String email, UserProfiles userProfiles) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.userProfiles = userProfiles;
    }


}
