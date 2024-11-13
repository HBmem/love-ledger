package love.loveledger.domain;

import love.loveledger.data.LoveInterestRepository;
import love.loveledger.data.RelationshipRepository;
import love.loveledger.data.UserRepository;
import love.loveledger.models.Relationship;
import love.loveledger.models.AppUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationshipService {
    private final RelationshipRepository relationshipRepository;
    private final UserRepository userRepository;
    private final LoveInterestRepository loveInterestRepository;

    public RelationshipService(RelationshipRepository relationshipRepository, UserRepository userRepository, LoveInterestRepository loveInterestRepository) {
        this.relationshipRepository = relationshipRepository;
        this.userRepository = userRepository;
        this.loveInterestRepository = loveInterestRepository;
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
            result.addMessage("Relationship ID cannot be set for an 'add' operation", ResultType.INVALID);
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
            String msg = String.format("Relationship ID: %s, not found", relationship.getRelationshipId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int relationshipId) {
        return relationshipRepository.delete(relationshipId);
    }

    private Result<Relationship> validate(Relationship relationship) {
        Result<Relationship> result = new Result<>();

        if (relationship == null) {
            result.addMessage("Relationship cannot be null", ResultType.INVALID);
            return result;
        }

        if (relationship.getRelationshipStatus() == null) {
            result.addMessage("Relationship status cannot be null", ResultType.INVALID);
        }

        if (relationship.getImportanceLevel() < 1 || relationship.getImportanceLevel() > 5) {
            result.addMessage("Importance level must between 1 and 5", ResultType.INVALID);
        }

        if (relationship.getStartDate() == null) {
            result.addMessage("Start date is required", ResultType.INVALID);
            return result;
        }

        if (Validations.endBeforeStart(relationship.getStartDate(), relationship.getEndDate())) {
            result.addMessage("Start date must be before end date", ResultType.INVALID);
        }

        if (userRepository.findByUserId(relationship.getUserId()) == null) {
            result.addMessage("User does not exist", ResultType.INVALID);
        }

        if (loveInterestRepository.findLoveInterestById(relationship.getLoveInterestId()) == null) {
            result.addMessage("Love interest does not exist", ResultType.INVALID);
        }

        return result;
    }
}
