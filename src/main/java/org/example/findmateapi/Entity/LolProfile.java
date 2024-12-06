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
public class LolProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "lolProfile")
    @JsonBackReference
    private UserProfiles userProfiles;

    private Integer rank;
    private String summonerName;
    /** Ranks:
     *      IRON_I(4), IRON_II(3), IRON_III(2), IRON_IV(1),
     *      BRONZE_I(8), BRONZE_II(7), BRONZE_III(6), BRONZE_IV(5),
     *      SILVER_I(12), SILVER_II(11), SILVER_III(10), SILVER_IV(9),
     *      GOLD_I(16), GOLD_II(15), GOLD_III(14), GOLD_IV(13),
     *      PLATINUM_I(20), PLATINUM_II(19), PLATINUM_III(18), PLATINUM_IV(17),
     *      EMERALD_I(24), EMERALD_II(23), EMERALD_III(22), EMERALD_IV(21),
     *      DIAMOND_I(28), DIAMOND_II(27), DIAMOND_III(26), DIAMOND_IV(25),
     *      MASTER(29), GRANDMASTER(30), CHALLENGER(31);
     */

    private Boolean roleTop;
    private Boolean roleJungle;
    private Boolean roleMid;
    private Boolean roleAdc;
    private Boolean roleSupport;

    private LocalDateTime lastRefreshed;

    public LolProfile(UserProfiles userProfiles, Integer rank){
        this.rank = rank;
        this.userProfiles = userProfiles;
        this.lastRefreshed = LocalDateTime.now();
    }





}
