package love.loveledger.domain;

import love.loveledger.data.OutingRepository;
import love.loveledger.data.RelationshipRepository;
import love.loveledger.models.Outing;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutingService {
    private final OutingRepository outingRepository;
    private final RelationshipRepository relationshipRepository;

    public OutingService(OutingRepository outingRepository, RelationshipRepository relationshipRepository) {
        this.outingRepository = outingRepository;
        this.relationshipRepository = relationshipRepository;
    }

    public Outing findByOutingId(int outingId) {
        return outingRepository.findOutingById(outingId);
    }

    public List<Outing> findAllOutingsByRelationshipId(int relationshipId) {
        return outingRepository.findAllOutingsByRelationshipId(relationshipId);
    }

    public Result<Outing> add(Outing outing) {
        Result<Outing> result = validate(outing);

        if (!result.isSuccess()) {
            return result;
        }

        if (outing.getOutingId() != 0) {
            result.addMessage("Outing ID cannot be set for an 'add' operation", ResultType.INVALID);
            return result;
        }

        outing = outingRepository.add(outing);
        result.setPayload(outing);
        return result;
    }

    public Result<Outing> update(Outing outing) {
        Result<Outing> result = validate(outing);

        if (!result.isSuccess()) {
            return result;
        }

        if (outing.getOutingId() <= 0) {
            result.addMessage("Outing ID must be set for an 'update' operations", ResultType.INVALID);
            return result;
        }

        if (!outingRepository.update(outing)) {
            String msg = String.format("Outing ID: %s not found", outing.getOutingId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int outingId) {
        return outingRepository.delete(outingId);
    }

    private Result<Outing> validate(Outing outing) {
        Result<Outing> result = new Result<>();

        if (Validations.isNullOrBlank(outing.getName())) {
            result.addMessage("Name must not be blank", ResultType.INVALID);
        }

        if (Validations.endBeforeStart(outing.getStartTime(), outing.getEndTime())) {
            result.addMessage("Start time must be before end time", ResultType.INVALID);
        }

        if (outing.getType() == null) {
            result.addMessage("Outing type must be selected", ResultType.INVALID);
        }

        if (relationshipRepository.findRelationshipById(outing.getRelationshipId()) == null) {
            result.addMessage("Relationship does not exist", ResultType.INVALID);
        }

        return result;
    }
}
