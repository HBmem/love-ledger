package love.loveledger.domain;

import love.loveledger.data.RelationshipRepository;
import love.loveledger.models.Relationship;
import love.loveledger.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationshipService {
    private final RelationshipRepository relationshipRepository;
//    private final

    public RelationshipService(RelationshipRepository repository) {
        this.relationshipRepository = repository;
    }

    public List<Relationship> findAllRelationshipByUserId(int userId) {
        return relationshipRepository.findAllRelationshipByUserId(userId);
    }

    public Relationship findRelationshipById(int relationshipId) {
        return relationshipRepository.findRelationshipById(relationshipId);
    }

    public Result<Relationship> add(Relationship relationship) {
        Result<Relationship> result = validate(relationship);

        if (!result.isSuccess()) {
            return result;
        }

        if (relationship.getRelationshipId() != 0) {
            result.addMessage("relationshipId cannot be set fro 'add' operation", ResultType.INVALID);
            return result;
        }

        relationship = relationshipRepository.add(relationship);
        result.setPayload(relationship);
        return result;
    }

    public Result<Relationship> update(Relationship relationship) {
        Result<Relationship> result = validate(relationship);

        if (!result.isSuccess()) {
            return result;
        }

        if (relationship.getRelationshipId() <= 0) {
            result.addMessage("Relationship ID must be set for 'update' operation", ResultType.INVALID);
            return result;
        }

        if (!relationshipRepository.update(relationship)) {
            String msg = String.format("relationshipId: %s, not found", relationship.getRelationshipId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int agentId) {
        return relationshipRepository.delete(agentId);
    }

    private Result<Relationship> validate(Relationship relationship) {
        Result<Relationship> result = new Result<>();

        if (relationship == null) {
            result.addMessage("relationship cannot be null", ResultType.INVALID);
            return result;
        }

        if (relationship.getStartDate() == null) {
            result.addMessage("start date is required", ResultType.INVALID);
            return result;
        }

        if (Validations.endBeforeStart(relationship.getStartDate(), relationship.getEndDate())) {
            result.addMessage("start date must be before end date", ResultType.INVALID);
        }

        return result;
    }
}
