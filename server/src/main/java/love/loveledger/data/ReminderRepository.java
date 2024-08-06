package love.loveledger.data;

import love.loveledger.models.Reminder;

import java.util.List;

public interface ReminderRepository {
    List<Reminder> findAllReminderByUserId(int userId);

    List<Reminder> findAllReminderByRelationshipId(int relationshipId);

    Reminder findReminderById(int reminderId);

    Reminder add(Reminder reminder);

    boolean update(Reminder reminder);

    boolean delete(int reminder_id);
}
