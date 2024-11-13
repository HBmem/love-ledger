package love.loveledger.domain;

import love.loveledger.data.NotableDayRepository;
import love.loveledger.data.RelationshipRepository;
import love.loveledger.data.ReminderRepository;
import love.loveledger.data.UserRepository;
import love.loveledger.models.Reminder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReminderService {
    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;
    private final RelationshipRepository relationshipRepository;
    private final NotableDayRepository notableDayRepository;

    public ReminderService(ReminderRepository reminderRepository, UserRepository userRepository, RelationshipRepository relationshipRepository, NotableDayRepository notableDayRepository) {
        this.reminderRepository = reminderRepository;
        this.userRepository = userRepository;
        this.relationshipRepository = relationshipRepository;
        this.notableDayRepository = notableDayRepository;
    }

    public List<Reminder> findAllReminderByUserId(int userId) {
        return reminderRepository.findAllReminderByUserId(userId);
    }

    public List<Reminder> findAllReminderByRelationshipId(int relationshipId) {
        return reminderRepository.findAllReminderByRelationshipId(relationshipId);
    }

    public Reminder findReminderById(int reminderId) {
        return reminderRepository.findReminderById(reminderId);
    }

    public Result<Reminder> add(Reminder reminder) {
        Result<Reminder> result = validate(reminder);

        if (!result.isSuccess()) {
            return result;
        }

        if (reminder.getReminderId() != 0) {
            result.addMessage("Reminder ID cannot be set for an 'add' operation", ResultType.INVALID);
            return result;
        }

        reminder = reminderRepository.add(reminder);
        result.setPayload(reminder);
        return result;
    }

    public Result<Reminder> update(Reminder reminder) {
        Result<Reminder> result = validate(reminder);

        if (!result.isSuccess()) {
            return result;
        }

        if (reminder.getReminderId() <= 0) {
            result.addMessage("Reminder ID must be set for an 'update' operation", ResultType.INVALID);
            return result;
        }

        if (!reminderRepository.update(reminder)) {
            String msg = String.format("Reminder ID: %s, not found", reminder.getReminderId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int reminderId) { return reminderRepository.delete(reminderId); }

    private Result<Reminder> validate(Reminder reminder) {
        Result<Reminder> result = new Result<>();

        if (reminder == null) {
            result.addMessage("Reminder cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(reminder.getName())) {
            result.addMessage("Name is required", ResultType.INVALID);
        }

        if (reminder.getDate() == null) {
            result.addMessage("Date is required", ResultType.INVALID);
            return result;
        }

        if (reminder.getDate().isBefore(LocalDate.now())) {
            result.addMessage("Date must be in the future", ResultType.INVALID);
        }

        if (userRepository.findByUserId(reminder.getUserId()) == null) {
            result.addMessage("User does not exist", ResultType.INVALID);
        }

        if (reminder.getRelationshipId() > 0) {
            if (relationshipRepository.findRelationshipById(reminder.getRelationshipId()) == null) {
                result.addMessage("Relationship does not exist", ResultType.INVALID);
            }
        }

        if (reminder.getNotableDayId() > 0) {
            if (notableDayRepository.findById(reminder.getNotableDayId()) == null) {
                result.addMessage("Notable day does not exist", ResultType.INVALID);
            }
        }

        return result;
    }
}
