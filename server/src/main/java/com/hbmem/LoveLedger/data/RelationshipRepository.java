package com.hbmem.LoveLedger.data;

import com.hbmem.LoveLedger.model.Relationship;

import java.util.List;

public interface RelationshipRepository {
    Relationship findByRelationshipById(int id);

    List<Relationship> findAllRelationshipByUserId(int userId);

    List<Relationship> findAllRelationshipByLoveInterestId(int loveInterestId);

    Relationship add(Relationship relationship);

    boolean update(Relationship relationship);

    boolean delete(int id);
}
