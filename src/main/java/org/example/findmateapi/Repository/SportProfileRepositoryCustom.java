package org.example.findmateapi.Repository;

import org.example.findmateapi.Entity.SportProfile;
import org.example.findmateapi.Request.FilterSportProfilesRequest;

import java.util.List;

public interface SportProfileRepositoryCustom {
    List<SportProfile> filterSportProfiles(FilterSportProfilesRequest request);
}
