package org.example.findmateapi.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.findmateapi.Entity.Sport;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class ProfileSportResponse {
    private Long id;
    private String username;
    private Set<Sport> sports;
    private String city;

}
