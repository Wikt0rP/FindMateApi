package org.example.findmateapi.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.findmateapi.Entity.Cs2Profile;
import org.example.findmateapi.Entity.LolProfile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfilesLolResponse {
    private Long userId;
    private String username;
    private String email;
    //private UserProfiles userProfiles;
    // null if fullInfo = false
    private LolProfile lolProfile;



}
