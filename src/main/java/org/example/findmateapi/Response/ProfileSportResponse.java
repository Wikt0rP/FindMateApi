package org.example.findmateapi.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class ProfileSportResponse {
    private Long id;
    private String username;
    private String sports;
    private String city;

}
