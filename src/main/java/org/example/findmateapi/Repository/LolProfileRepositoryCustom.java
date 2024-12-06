package org.example.findmateapi.Repository;

import org.example.findmateapi.Entity.LolProfile;
import org.example.findmateapi.Request.FilterLolProfilesRequest;

import java.util.List;

public interface LolProfileRepositoryCustom {
    List<LolProfile> filterLolProfiles(FilterLolProfilesRequest request);
}
