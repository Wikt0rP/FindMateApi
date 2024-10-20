package org.example.findmateapi.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Cs2Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "cs2Profile")
    @JsonBackReference
    private User user;

    private Integer primeRank;

    public Cs2Profile(User user, Integer primeRank) {
        this.user = user;
        this.primeRank = primeRank;
    }
    public Cs2Profile(User user){
        this.user = user;
        this.primeRank = 10;//default prime rank
    }

}
