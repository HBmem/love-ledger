package com.hbmem.LoveLedger.domain;

import com.hbmem.LoveLedger.data.LoveInterestRepository;
import com.hbmem.LoveLedger.data.RelationshipRepository;
import com.hbmem.LoveLedger.data.UserCredentialRepository;
import com.hbmem.LoveLedger.model.Relationship;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationshipService {
    private final RelationshipRepository repository;
    private final UserCredentialRepository userCredentialRepository;
    private final LoveInterestRepository loveInterestRepository;

    public RelationshipService(RelationshipRepository repository, UserCredentialRepository userCredentialRepository, LoveInterestRepository loveInterestRepository) {
        this.repository = repository;
        this.userCredentialRepository = userCredentialRepository;
        this.loveInterestRepository = loveInterestRepository;
    }

    public Relationship findRelationshipById(int id) {
        return repository.findByRelationshipById(id);
    }

    public List<Relationship> findAllRelationshipByUserId(int userId) {
        return repository.findAllRelationshipByUserId(userId);
    }
    
    public List<Relationship> findAllRelationshipByLoveInterestId(int loveInterestId) {
        return repository.findAllRelationshipByLoveInterestId(loveInterestId);
    }
    
    public Result<Relationship> add(Relationship relationship) {
        Result<Relationship> result = validate(relationship);
        
        if (!result.isSuccess()) {
            return result;
        }
        
        if (relationship.getId() != 0) {
            result.addMessage("Relationship Id cannot be set for 'add' operations", ResultType.INVALID);
            return result;
        }
        
        relationship = repository.add(relationship);
        result.setPayload(relationship);
        return result;
    }

    public Result<Relationship> update(Relationship relationship) {
        Result<Relationship> result = validate(relationship);

        if (!result.isSuccess()) {
            return result;
        }

        if (relationship.getId() <= 0) {
            result.addMessage("Relationship Id must be set for 'update' operations", ResultType.INVALID);
        }

        if (!repository.update(relationship)) {
            String msg = String.format("Relationship: Id: %s, not found", relationship.getId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean delete(int id) {
        return repository.delete(id);
    }

    private Result<Relationship> validate(Relationship relationship) {
        Result<Relationship> result = new Result<>();

        if (relationship == null) {
            result.addMessage("Relationship cannot be null", ResultType.INVALID);
            return result;
        }

        if (relationship.getStartDate() == null) {
            result.addMessage("Start date cannot be null", ResultType.INVALID);
        }

        if (relationship.getEndDate() != null && relationship.getStartDate() != null) {
            if (Validations.endBeforeStart(relationship.getStartDate(), relationship.getEndDate())) {
                result.addMessage("End date must be after the start date", ResultType.INVALID);
            }
        }

        if (relationship.getRelationshipStatus() == null) {
            result.addMessage("Relationship status must be set", ResultType.INVALID);
        }

        if (relationship.getImportanceLevel() < 1 || relationship.getImportanceLevel() > 5) {
            result.addMessage("Importance level must be between 1 and 5", ResultType.INVALID);
        }

        if (userCredentialRepository.findByUserId(relationship.getUserId()) == null) {
            result.addMessage("User does not exist", ResultType.INVALID);
        }

        if (loveInterestRepository.findLoveInterestById(relationship.getLoveInterestId()) == null) {
            result.addMessage("Love interest does not exist", ResultType.INVALID);
        }

        return result;
    }
}
