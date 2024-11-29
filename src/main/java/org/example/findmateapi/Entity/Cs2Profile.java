package org.example.findmateapi.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Cs2Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "cs2Profile")
    private UserProfiles userProfiles;

    private Integer primeRank;

    //Last time player was looking to play / created a profile
    private LocalDateTime lastRefreshed;

    public Cs2Profile(UserProfiles userProfiles, Integer primeRank) {
        this.userProfiles = userProfiles;
        this.primeRank = primeRank;
        this.lastRefreshed = LocalDateTime.now();
    }
    public Cs2Profile(UserProfiles userProfiles){
        this.userProfiles = userProfiles;
        this.primeRank = 10;//default prime rank
        this.lastRefreshed = LocalDateTime.now();
    }

}
