package love.loveledger.data;

import love.loveledger.models.Relationship;

import java.util.List;

public interface RelationshipRepository {
    List<Relationship> findAllUserRelationship(int userId);

    Relationship add(Relationship relationship);

    boolean update(Relationship relationship);

    boolean delete(int relationshipId);
}
