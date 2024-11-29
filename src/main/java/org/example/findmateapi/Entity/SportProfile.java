package org.example.findmateapi.Entity;

import jakarta.persistence.*;

@Entity
public class SportProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "sportProfile")
    private UserProfiles userProfiles;

}
