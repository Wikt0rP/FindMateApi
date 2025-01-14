package org.example.findmateapi.Request;

import lombok.Getter;
import lombok.Setter;
import org.example.findmateapi.Entity.City;

import java.util.List;

@Getter
@Setter
public class CreateSportProfileRequest {
    private String sport;
    private String city;

}
