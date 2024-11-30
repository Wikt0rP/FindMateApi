package org.example.findmateapi.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.findmateapi.Entity.Cs2Profile;
import org.example.findmateapi.Request.FilterCs2ProfilesRequest;

import java.util.ArrayList;
import java.util.List;

public class Cs2ProfileRepositoryImpl implements Cs2ProfileRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Cs2Profile> filterCs2Profiles(FilterCs2ProfilesRequest request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cs2Profile> query = cb.createQuery(Cs2Profile.class);
        Root<Cs2Profile> root = query.from(Cs2Profile.class);

        List<Predicate> predicates = new ArrayList<>();
        if (request.getMinPrimeRank() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("primeRank"), request.getMinPrimeRank()));
        }
        if (request.getMaxPrimeRank() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("primeRank"), request.getMaxPrimeRank()));
        }
        if (request.getLastRefreshed() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("lastRefreshed"), request.getLastRefreshed()));
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(root.get("lastRefreshed")));
        return entityManager.createQuery(query).getResultList();
    }

}
