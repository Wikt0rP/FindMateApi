package org.example.findmateapi.Response;

import lombok.Getter;
import lombok.Setter;
import org.example.findmateapi.Entity.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private Set<Role> roles;

    public JwtResponse(String accessToken, Long id, String username, String email, Set<Role> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}