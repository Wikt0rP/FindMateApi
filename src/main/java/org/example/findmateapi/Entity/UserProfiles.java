package org.example.findmateapi.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserProfiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "userProfiles")
    private User user;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "faceit_profile_id", referencedColumnName = "id")
    private FaceitProfile faceitProfile;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "riot_profile_id", referencedColumnName = "id")
    private RiotProfile riotProfile;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "steam_profile_id", referencedColumnName = "id")
    private SteamProfile steamProfile;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cs2_profile_id", referencedColumnName = "id")
    private Cs2Profile cs2Profile;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sport_profile_id", referencedColumnName = "id")
    private SportProfile sportProfile;

    public UserProfiles(User user) {
        this.user = user;
    }
}
