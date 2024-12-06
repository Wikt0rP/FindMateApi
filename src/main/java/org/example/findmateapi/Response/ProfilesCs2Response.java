package org.example.findmateapi.Response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.findmateapi.Entity.Cs2Profile;

//@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfilesCs2Response {

    private Long userId;
    private String username;
    private String email;
    //private UserProfiles userProfiles;
    // null if fullInfo = false
    private Cs2Profile cs2Profile;


    public ProfilesCs2Response(Long userId, String username, String email, Cs2Profile cs2Profile) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.cs2Profile = cs2Profile;
    }

}
