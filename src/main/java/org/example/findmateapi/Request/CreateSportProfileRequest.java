package org.example.findmateapi.Request;

import lombok.Getter;
import lombok.Setter;
import org.example.findmateapi.Entity.City;
import org.example.findmateapi.Entity.Sport;

import java.util.List;

@Getter
@Setter
public class CreateSportProfileRequest {
    private Sport sport;
    private String city;

}
