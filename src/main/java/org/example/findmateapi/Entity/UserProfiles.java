package org.example.findmateapi.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonBackReference(value = "user-userprofiles")
    private User user;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "faceit_profile_id", referencedColumnName = "id")
    @JsonManagedReference(value = "userprofiles-faceitprofile")
    private FaceitProfile faceitProfile;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "lol_profile_id", referencedColumnName = "id")
    @JsonManagedReference(value = "userprofiles-lolprofile")
    private LolProfile lolProfile;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "steam_profile_id", referencedColumnName = "id")
    @JsonManagedReference(value = "userprofiles-steamprofile")
    private SteamProfile steamProfile;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cs2_profile_id", referencedColumnName = "id")
    @JsonManagedReference(value = "userprofiles-cs2profile")
    private Cs2Profile cs2Profile;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sport_profile_id", referencedColumnName = "id")
    @JsonManagedReference(value = "userprofiles-sportprofile")
    private SportProfile sportProfile;

    public UserProfiles(User user) {
        this.user = user;
    }
}
