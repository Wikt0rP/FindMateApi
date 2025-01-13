package org.example.findmateapi.Request;

import lombok.Getter;
import lombok.Setter;
import org.example.findmateapi.Entity.Sport;

@Getter
@Setter
public class FilterSportProfilesRequest {
    private String baseCity;
    private Integer radiusKm;
    private Sport sport;
    private Boolean fullInfo;
}