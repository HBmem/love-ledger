package love.loveledger.domain;

import love.loveledger.data.LoveInterestRepository;
import love.loveledger.data.RelationshipRepository;
import love.loveledger.data.UserRepository;
import love.loveledger.models.LoveInterest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoveInterestService {
    private final LoveInterestRepository loveInterestRepository;
    private final UserRepository userRepository;

    public LoveInterestService(LoveInterestRepository loveInterestRepository, UserRepository userRepository) {
        this.loveInterestRepository = loveInterestRepository;
        this.userRepository = userRepository;
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
            result.addMessage("Love interest ID cannot be set for an 'add' operation", ResultType.INVALID);
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
            result.addMessage("Love Interest ID must be set for an 'update' operation", ResultType.INVALID);
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
            result.addMessage("Must have a nickname or a firstname", ResultType.INVALID);
        }

        LocalDate today = LocalDate.now();
        if (loveInterest.getBirthday().isAfter(today)) {
            result.addMessage("Birthday must be after today's date", ResultType.INVALID);
        }

        if (userRepository.findByUserId(loveInterest.getUserId()) == null) {
            result.addMessage("User does not exist", ResultType.INVALID);
        }

        return result;
    }
}
