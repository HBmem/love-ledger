package love.loveledger.data;

import love.loveledger.models.Outing;

import java.util.List;

public interface OutingRepository {
    List<Outing> findAllOutingsByRelationshipId(int relationshipId);

    Outing findOutingById(int outingId);

    Outing add(Outing outing);

    boolean update(Outing outing);

    boolean delete(int outingId);
}
