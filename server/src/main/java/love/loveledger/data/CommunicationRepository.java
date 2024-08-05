package love.loveledger.data;

import love.loveledger.models.Communication;

import java.util.List;

public interface CommunicationRepository {
    List<Communication> findAllCommunicationByRelationshipId(int userId);

    Communication findCommunicationById(int communicationId);

    Communication add(Communication communication);

    boolean update(Communication communication);

    boolean delete(int communication_id);
}
