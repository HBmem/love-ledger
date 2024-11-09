package love.loveledger.domain;

import love.loveledger.data.CommunicationRepository;
import love.loveledger.data.RelationshipRepository;
import love.loveledger.models.Communication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunicationService {
    private final CommunicationRepository communicationRepository;
    private final RelationshipRepository relationshipRepository;

    public CommunicationService(CommunicationRepository communicationRepository, RelationshipRepository relationshipRepository) {
        this.communicationRepository = communicationRepository;
        this.relationshipRepository = relationshipRepository;
    }

    public List<Communication> findAllCommunicationByRelationshipId(int relationshipId) {
        return communicationRepository.findAllCommunicationByRelationshipId(relationshipId);
    }

    public Communication findCommunicationById(int communicationId) {
        return communicationRepository.findCommunicationById(communicationId);
    }

    public Result<Communication> add(Communication communication) {
        Result<Communication> result = validate(communication);

        if (!result.isSuccess()) {
            return result;
        }

        if (communication.getCommunicationId() != 0) {
            result.addMessage("communicationId cannot be set for 'add' operations", ResultType.INVALID);
            return result;
        }

        communication = communicationRepository.add(communication);
        result.setPayload(communication);
        return result;
    }

    public Result<Communication> update(Communication communication) {
        Result<Communication> result = validate(communication);

        if (!result.isSuccess()) {
            return result;
        }

        if (communication.getCommunicationId() <= 0) {
            result.addMessage("Communication Id must be set for 'update' operation", ResultType.INVALID);
            return result;
        }

        if (!communicationRepository.update(communication)) {
            String msg = String.format("communicationId: %s, not found", communication.getCommunicationId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int communicationId) { return communicationRepository.delete(communicationId); }

    private Result<Communication> validate(Communication communication) {
        Result<Communication> result = new Result<>();

        if (communication == null) {
            result.addMessage("Communication cannot be null", ResultType.INVALID);
            return result;
        }

        if (communication.getType() == null) {
            result.addMessage("Communication type cannot be null", ResultType.INVALID);
            return result;
        }

        if (communication.getDate() == null) {
            result.addMessage("Communication date cannot be null", ResultType.INVALID);
            return result;
        }

        if (communication.getMoodScore() <= 1 || communication.getMoodScore() > 5) {
            result.addMessage("Mood score must be between 1 and 5", ResultType.INVALID);
        }

        if (relationshipRepository.findRelationshipById(communication.getRelationshipId()) == null) {
            result.addMessage("Relationship does not exist", ResultType.INVALID);
        }

        return result;
    }
}
