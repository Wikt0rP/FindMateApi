package org.example.findmateapi.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    private String googleId;

    private boolean isGoogleUser;

    private boolean isActive;

    private String confirmationCode;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonManagedReference
    private Set<Role> roles = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_profiles_id", referencedColumnName = "id")
    @JsonManagedReference(value = "user-userprofiles")
    private UserProfiles userProfiles;

    @ManyToMany
    @JoinTable(
            name = "user_teams",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    @JsonManagedReference
    private List<Team> teams = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Invitation> invitations;


    public User(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        isActive = false;
        roles.add(role);
    }
    public User(String username, String email, String googleId, boolean isGoogleUser, Role role) {
        this.username = username;
        this.email = email;
        this.googleId = googleId;
        this.isGoogleUser = isGoogleUser;
        isActive = false;
        roles.add(role);
    }

    public void addTeam(Team team) {
        this.teams.add(team);
        team.getUsers().add(this);
    }
}
