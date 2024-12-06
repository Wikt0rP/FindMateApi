package org.example.findmateapi.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateLolProfileRequest {
    private Integer rank;
    private String summonerName;
    private Boolean roleTop;
    private Boolean roleJungle;
    private Boolean roleMid;
    private Boolean roleAdc;
    private Boolean roleSupport;
}
