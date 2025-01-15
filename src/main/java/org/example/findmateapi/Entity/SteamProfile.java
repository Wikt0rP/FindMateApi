package org.example.findmateapi.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SteamProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "steamProfile")
    @JsonBackReference(value = "userprofiles-steamprofile")
    private UserProfiles userProfiles;

    private String steamId;




}
