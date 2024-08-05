package love.loveledger.data;

import love.loveledger.models.Relationship;

import java.util.List;

public interface RelationshipRepository {
    List<Relationship> findAllRelationshipByUserId(int userId);

    Relationship findRelationshipById(int relationshipId);

    Relationship add(Relationship relationship);

    boolean update(Relationship relationship);

    boolean delete(int relationshipId);
}
