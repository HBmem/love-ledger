package love.loveledger.domain;

import love.loveledger.data.MilestoneRepository;
import love.loveledger.models.Milestone;
import love.loveledger.models.Relationship;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MilestoneService {
    private final MilestoneRepository milestoneRepository;

    public MilestoneService(MilestoneRepository milestoneRepository) {
        this.milestoneRepository = milestoneRepository;
    }

    public List<Milestone> finaAllMilestone() {
        return milestoneRepository.findAllMilestone();
    }

    public Milestone findMilestoneById(int milestoneId) {
        return milestoneRepository.findMilestoneById(milestoneId);
    }

    public Result<Milestone> add(Milestone milestone) {
        Result<Milestone> result = validate(milestone);

        if (!result.isSuccess()) {
            return result;
        }

        if (milestone.getMilestoneId() != 0) {
            result.addMessage("milestoneId cannot be set for 'add' operation", ResultType.INVALID);
            return result;
        }

        milestone = milestoneRepository.add(milestone);
        result.setPayload(milestone);
        return result;
    }

    public Result<Milestone> update(Milestone milestone) {
        Result<Milestone> result = validate(milestone);

        if (!result.isSuccess()) {
            return result;
        }

        if (milestone.getMilestoneId() <= 0) {
            result.addMessage("Milestone ID must be set for 'update' operations", ResultType.INVALID);
            return result;
        }

        if (!milestoneRepository.update(milestone)) {
            String msg = String.format("Milestone ID: %s, not found", milestone.getMilestoneId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int milestoneId) {
        return milestoneRepository.delete(milestoneId);
    }

    private Result<Milestone> validate(Milestone milestone) {
        Result<Milestone> result = new Result<>();

        if (milestone == null) {
            result.addMessage("milestone cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(milestone.getName())) {
            result.addMessage("name is required", ResultType.INVALID);
            return result;
        }

        if (milestone.getType() == null) {
            result.addMessage("milestone type cannot be null", ResultType.INVALID);
        }

        List<Milestone> milestones = milestoneRepository.findAllMilestone();
        if (milestones.stream().anyMatch(m -> m.getName().equalsIgnoreCase(milestone.getName()))) {
            result.addMessage("name must be unique", ResultType.INVALID);
        }

        if (Validations.isNullOrBlank(milestone.getDescription())) {
            result.addMessage("description is required", ResultType.INVALID);
        }

        return result;
    }
}
