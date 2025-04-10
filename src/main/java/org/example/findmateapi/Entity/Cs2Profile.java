package org.example.findmateapi.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cs2Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "cs2Profile")
    @JsonBackReference
    private UserProfiles userProfiles;

    private String steamLink;

    private Integer primeRank;

    //Last time player was looking to play / created a profile
    private LocalDateTime lastRefreshed;

    public Cs2Profile(UserProfiles userProfiles, Integer primeRank, String steamLink) {
        this.userProfiles = userProfiles;
        this.primeRank = primeRank;
        this.steamLink = steamLink;
        this.lastRefreshed = LocalDateTime.now();
    }
    public Cs2Profile(UserProfiles userProfiles){
        this.userProfiles = userProfiles;
        this.primeRank = 10;//default prime rank
        this.lastRefreshed = LocalDateTime.now();
    }

}
