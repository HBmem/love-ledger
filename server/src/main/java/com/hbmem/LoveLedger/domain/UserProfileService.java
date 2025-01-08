package com.hbmem.LoveLedger.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hbmem.LoveLedger.data.UserCredentialRepository;
import com.hbmem.LoveLedger.data.UserProfileRepository;
import com.hbmem.LoveLedger.model.UserProfile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserProfileService {
    private final UserProfileRepository repository;
    private final UserCredentialRepository userCredentialRepository;

    public UserProfileService(UserProfileRepository repository, UserCredentialRepository userCredentialRepository) {
        this.repository = repository;
        this.userCredentialRepository = userCredentialRepository;
    }

    public UserProfile findByUserProfileId(int id) {
        return repository.findByUserProfileId(id);
    }

    public Result<UserProfile> add(UserProfile userProfile) throws JsonProcessingException {
        Result<UserProfile> result = validate(userProfile);

        if (!result.isSuccess()) {
            return result;
        }

        if (userProfile.getId() != 0) {
            result.addMessage("User Profile Id cannot be set for 'add' operations", ResultType.INVALID);
            return result;
        }

        userProfile = repository.add(userProfile);
        result.setPayload(userProfile);
        return result;
    }

    public Result<UserProfile> update(UserProfile userProfile) {
        Result<UserProfile> result = validate(userProfile);

        if (!result.isSuccess()) {
            return result;
        }

        if (userProfile.getId() <= 0) {
            result.addMessage("User Profile Id must be set for 'update' operations", ResultType.INVALID);
            return result;
        }

        if (!repository.update(userProfile)) {
            String msg = String.format("UserProfile Id: %s, not found ", userProfile.getId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean delete(int id) {
        return repository.delete(id);
    }

    private Result<UserProfile> validate(UserProfile userProfile) {
        Result<UserProfile> result = new Result<>();

        if (Validations.isNullOrBlank(userProfile.getFirstName())) {
            result.addMessage("First name must not be null", ResultType.INVALID);
        }

        if (userProfile.getGender() == null) {
            result.addMessage("User does not have a gender", ResultType.INVALID);
        }

        if (userProfile.getBirthday() == null) {
            result.addMessage("User does not have a valid birthday", ResultType.INVALID);
        }

        if (!result.isSuccess()) {
            return result;
        }

        if (userProfile.getBirthday().isAfter(LocalDate.now().minusYears(18))) {
            result.addMessage("User must be older than 18 years old", ResultType.INVALID);
        }

        if (userCredentialRepository.findByUserId(userProfile.getId()) == null) {
            result.addMessage("User credential with this id does not exist", ResultType.INVALID);
        }

        return result;
    }
}
