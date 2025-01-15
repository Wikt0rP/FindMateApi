package org.example.findmateapi.Request;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FilterSportProfilesRequest {
    private String baseCity;
    private Integer radiusKm;
    private String sport;
    private Boolean fullInfo;
}