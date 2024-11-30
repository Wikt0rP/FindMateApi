package org.example.findmateapi.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilterCs2ProfilesRequest {
    private Integer minPrimeRank;
    private Integer maxPrimeRank;
    private LocalDateTime lastRefreshed;
}
