package com.hbmem.LoveLedger.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hbmem.LoveLedger.data.LoveInterestRepository;
import com.hbmem.LoveLedger.data.UserCredentialRepository;
import com.hbmem.LoveLedger.model.LoveInterest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoveInterestService {
    private final LoveInterestRepository repository;
    private final UserCredentialRepository userCredentialRepository;

    public LoveInterestService(LoveInterestRepository repository, UserCredentialRepository userCredentialRepository) {
        this.repository = repository;
        this.userCredentialRepository = userCredentialRepository;
    }

    public LoveInterest findLoveInterestById(int id) {
        return repository.findLoveInterestById(id);
    }

    public List<LoveInterest> findAllLoveInterestByUserId(int userId) {
        return repository.findAllLoveInterestByUserId(userId);
    }

    public Result<LoveInterest> add(LoveInterest loveInterest) throws JsonProcessingException {
        Result<LoveInterest> result = validate(loveInterest);

        if (!result.isSuccess()) {
            return result;
        }

        if (loveInterest.getId() != 0) {
            result.addMessage("Love Interest Id cannot be set for 'add' operations", ResultType.INVALID);
            return result;
        }

        loveInterest = repository.add(loveInterest);
        result.setPayload(loveInterest);
        return result;
    }

    public Result<LoveInterest> update(LoveInterest loveInterest) {
        Result<LoveInterest> result = validate(loveInterest);

        if (!result.isSuccess()) {
            return result;
        }

        if (loveInterest.getId() <= 0) {
            result.addMessage("Love Interest Id must be set for 'update' operations", ResultType.INVALID);
            return result;
        }

        if (!repository.update(loveInterest)) {
            String msg = String.format("LoveInterest Id: %s, not found", loveInterest.getId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean delete(int id) {
        return repository.delete(id);
    }

    private Result<LoveInterest> validate(LoveInterest loveInterest) {
        Result<LoveInterest> result = new Result<>();

        if (loveInterest == null) {
            result.addMessage("Love Interest cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(loveInterest.getNickname()) && Validations.isNullOrBlank(loveInterest.getFirstName())) {
            result.addMessage("Love Interest must have a nickname or a firstname", ResultType.INVALID);
        }

//        if (loveInterest.getNickname().length() > 50) {
//            result.addMessage("Nickname must not be longer than 50 characters", ResultType.INVALID);
//        }

        if (loveInterest.getGender() == null) {
            result.addMessage("Love Interest does not have a gender", ResultType.INVALID);
        }

        LocalDate today = LocalDate.now();
        if (loveInterest.getBirthday().isAfter(today)) {
            result.addMessage("Love Interest's birthday must have already happened", ResultType.INVALID);
        }

        if (userCredentialRepository.findByUserId(loveInterest.getUserId()) == null) {
            result.addMessage("User does not exist", ResultType.INVALID);
        }

        return result;
    }
}
