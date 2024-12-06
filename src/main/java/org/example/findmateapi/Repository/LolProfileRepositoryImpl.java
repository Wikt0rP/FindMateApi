package org.example.findmateapi.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.findmateapi.Entity.LolProfile;
import org.example.findmateapi.Request.FilterLolProfilesRequest;

import java.util.ArrayList;
import java.util.List;

public class LolProfileRepositoryImpl implements LolProfileRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<LolProfile> filterLolProfiles(FilterLolProfilesRequest request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<LolProfile> query = cb.createQuery(LolProfile.class);
        Root<LolProfile> root = query.from(LolProfile.class);

        List<Predicate> predicates = new ArrayList<>();
        if (request.getMinRank() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("rank"), request.getMinRank()));
        }

        if (request.getMaxRank() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("rank"), request.getMaxRank()));
        }


        List<Predicate> rolePredicates = new ArrayList<>();
        if (Boolean.TRUE.equals(request.getRoleTop())) {
            rolePredicates.add(cb.isTrue(root.get("roleTop")));
        }
        if (Boolean.TRUE.equals(request.getRoleJungle())) {
            rolePredicates.add(cb.isTrue(root.get("roleJungle")));
        }
        if (Boolean.TRUE.equals(request.getRoleMid())) {
            rolePredicates.add(cb.isTrue(root.get("roleMid")));
        }
        if (Boolean.TRUE.equals(request.getRoleAdc())) {
            rolePredicates.add(cb.isTrue(root.get("roleAdc")));
        }
        if (Boolean.TRUE.equals(request.getRoleSupport())) {
            rolePredicates.add(cb.isTrue(root.get("roleSupport")));
        }

        if (!rolePredicates.isEmpty()) {
            predicates.add(cb.or(rolePredicates.toArray(new Predicate[0])));
        }


        query.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getResultList();
    }
}
