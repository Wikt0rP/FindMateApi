package org.example.findmateapi.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonManagedReference
    private Set<Role> roles = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "faceit_profile_id", referencedColumnName = "id")
    @JsonManagedReference
    private FaceitProfile faceitProfile;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "riot_profile_id", referencedColumnName = "id")
    @JsonManagedReference
    private RiotProfile riotProfile;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "steam_profile_id", referencedColumnName = "id")
    @JsonManagedReference
    private SteamProfile steamProfile;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cs2_profile_id", referencedColumnName = "id")
    @JsonManagedReference
    private Cs2Profile cs2Profile;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sport_profile_id", referencedColumnName = "id")
    @JsonManagedReference
    private SportProfile sportProfile;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        isActive = false;
    }
    public User(String username, String email, String googleId, boolean isGoogleUser) {
        this.username = username;
        this.email = email;
        this.googleId = googleId;
        this.isGoogleUser = isGoogleUser;
        isActive = false;
    }

}
