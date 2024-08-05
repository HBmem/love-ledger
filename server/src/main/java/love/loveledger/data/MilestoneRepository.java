package love.loveledger.data;

import love.loveledger.models.Milestone;

import java.util.List;

public interface MilestoneRepository {
    List<Milestone> findAllMilestone();

    Milestone findMilestoneById(int milestoneId);

    Milestone add(Milestone milestone);

    boolean update(Milestone milestone);

    boolean delete(int milestone_id);
}
