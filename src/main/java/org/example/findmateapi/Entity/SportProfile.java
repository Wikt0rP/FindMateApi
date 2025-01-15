package org.example.findmateapi.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.findmateapi.Request.CreateSportProfileRequest;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SportProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "sportProfile")
    @JsonBackReference(value = "userprofiles-sportprofile")
    private UserProfiles userProfiles;

    private String city;


    private String sport;

    private LocalDateTime lastRefreshTime;

    public SportProfile(UserProfiles userProfiles, CreateSportProfileRequest createSportProfileRequest) {
        this.userProfiles = userProfiles;
        this.city = createSportProfileRequest.getCity();
        this.sport = createSportProfileRequest.getSport();
        this.lastRefreshTime = LocalDateTime.now();
    }

}
