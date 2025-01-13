package org.example.findmateapi.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.example.findmateapi.Entity.Sport;
import org.example.findmateapi.Entity.SportProfile;
import org.example.findmateapi.Request.FilterSportProfilesRequest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SportProfileRepositoryImpl implements SportProfileRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SportProfile> filterSportProfiles(FilterSportProfilesRequest request) {


        List<String> cityNamesInRadius = getCityNamesInRadius(request.getBaseCity(), request.getRadiusKm());


        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SportProfile> cq = cb.createQuery(SportProfile.class);
        Root<SportProfile> root = cq.from(SportProfile.class);

        List<Predicate> predicates = new ArrayList<>();


        if (!cityNamesInRadius.isEmpty()) {
            predicates.add(root.get("city").in(cityNamesInRadius));
        }

        if (request.getSport() != null && request.getSport().getId() != null) {

            Join<SportProfile, Sport> joinSports = root.join("sports", JoinType.INNER);
            predicates.add(cb.equal(joinSports.get("id"), request.getSport().getId()));
        }
        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }
        cq.orderBy(cb.desc(root.get("lastRefreshTime")));
        cq.distinct(true);
        return entityManager.createQuery(cq).getResultList();
    }


    private List<String> getCityNamesInRadius(String baseCity, Integer radiusKm) {
        if (baseCity == null || radiusKm == null) {
            return List.of();
        }
        return List.of(baseCity);
    }
}
