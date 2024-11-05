package love.loveledger.domain;

import love.loveledger.data.LoveInterestRepository;
import love.loveledger.data.RelationshipRepository;
import love.loveledger.models.LoveInterest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoveInterestService {
    private final LoveInterestRepository loveInterestRepository;
    private final RelationshipRepository relationshipRepository;

    public LoveInterestService(LoveInterestRepository loveInterestRepository, RelationshipRepository relationshipRepository) {
        this.loveInterestRepository = loveInterestRepository;
        this.relationshipRepository = relationshipRepository;
    }

    public List<LoveInterest> findAllLoveInterestByUserId(int userId) {
        return loveInterestRepository.findAllLoveInterestByUserId(userId);
    }

    public LoveInterest findLoveInterestById(int loveInterestId) {
        return loveInterestRepository.findLoveInterestById(loveInterestId);
    }

    public Result<LoveInterest> add(LoveInterest loveInterest) {
        Result<LoveInterest> result = validate(loveInterest);

        if (!result.isSuccess()) {
            return result;
        }

        if (loveInterest.getLoveInterestId() != 0) {
            result.addMessage("love interest cannot be set for 'add' operation", ResultType.INVALID);
            return result;
        }

        loveInterest = loveInterestRepository.add(loveInterest);
        result.setPayload(loveInterest);
        return result;
    }

    public Result<LoveInterest> update(LoveInterest loveInterest) {
        Result<LoveInterest> result = validate(loveInterest);

        if (!result.isSuccess()) {
            return result;
        }

        if (loveInterest.getLoveInterestId() <= 0) {
            result.addMessage("Love Interest ID must be set for 'update' operation", ResultType.INVALID);
            return result;
        }

        if (!loveInterestRepository.update(loveInterest)) {
            String msg = String.format("Love Interest ID: %s, not found", loveInterest.getLoveInterestId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int loveInterestId) {
        return loveInterestRepository.delete(loveInterestId);
    }

    private Result<LoveInterest> validate(LoveInterest loveInterest) {
        Result<LoveInterest> result = new Result<>();

        if (Validations.isNullOrBlank(loveInterest.getNickname()) && Validations.isNullOrBlank(loveInterest.getFirstName())) {
            result.addMessage("must have a nickname or a firstname", ResultType.INVALID);
        }

        LocalDate today = LocalDate.now();
        if (loveInterest.getBirthday().isAfter(today)) {
            result.addMessage("Birthday must be after today's date", ResultType.INVALID);
        }

//        if (loveInterest.getUserId())
//        return result;

        return result;
    }
}
