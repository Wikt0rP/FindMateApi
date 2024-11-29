package org.example.findmateapi.Entity;

import jakarta.persistence.*;

@Entity
public class RiotProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "riotProfile")
    private UserProfiles userProfiles;


}
