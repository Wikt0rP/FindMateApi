package org.example.findmateapi.Repository;

import org.example.findmateapi.Entity.Cs2Profile;
import org.example.findmateapi.Request.FilterCs2ProfilesRequest;

import java.util.List;

public interface Cs2ProfileRepositoryCustom {
    List<Cs2Profile> filterCs2Profiles(FilterCs2ProfilesRequest request);

}
