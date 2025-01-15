package org.example.findmateapi.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.findmateapi.Entity.SportProfile;
import org.example.findmateapi.Request.FilterSportProfilesRequest;
import org.example.findmateapi.Service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SportProfileRepositoryImpl implements SportProfileRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private CityService cityService;


    @Override
    public List<SportProfile> filterSportProfiles(FilterSportProfilesRequest request) {

        List<String> cityNamesInRadius = cityService.getStringListOfCitiesInRadius(request.getBaseCity(), request.getRadiusKm());

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SportProfile> cq = cb.createQuery(SportProfile.class);
        Root<SportProfile> root = cq.from(SportProfile.class);

        List<Predicate> predicates = new ArrayList<>();

        if (!cityNamesInRadius.isEmpty()) {
            predicates.add(root.get("city").in(cityNamesInRadius));
        }

        if (request.getSport() != null) {
            predicates.add(cb.equal(root.get("sport"), request.getSport()));
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        cq.orderBy(cb.desc(root.get("lastRefreshTime")));
        cq.distinct(true);
        return entityManager.createQuery(cq).getResultList();
    }
}