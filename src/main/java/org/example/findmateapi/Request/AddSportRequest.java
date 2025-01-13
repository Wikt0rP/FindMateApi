package org.example.findmateapi.Request;

import lombok.Getter;
import lombok.Setter;
import org.example.findmateapi.Entity.Sport;

@Getter
@Setter
public class AddSportRequest {
    private Sport sport;
}
