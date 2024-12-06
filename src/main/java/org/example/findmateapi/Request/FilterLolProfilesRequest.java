package org.example.findmateapi.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilterLolProfilesRequest {
    private Integer minRank;
    private Integer maxRank;
    private Boolean roleTop;
    private Boolean roleJungle;
    private Boolean roleMid;
    private Boolean roleAdc;
    private Boolean roleSupport;
    private Boolean fullInfo;
}
