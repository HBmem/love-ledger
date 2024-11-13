package love.loveledger.data;

import love.loveledger.models.Reminder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ReminderJdbcTemplateRepositoryTest {
    @Autowired
    ReminderJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() { knownGoodState.set(); }

    @Test
    void shouldFindAllReminderByUserId() {
        List<Reminder> reminders = repository.findAllReminderByUserId(1);
        assertNotNull(reminders);
        assertTrue(reminders.size() >= 1);
    }

    @Test
    void shouldNotFindAllRemindersByInvalidUserId() {
        List<Reminder> reminders = repository.findAllReminderByUserId(99);
        assertEquals(0, reminders.size());
    }

    @Test
    void shouldFindAllReminderByRelationshipId() {
        List<Reminder> reminders = repository.findAllReminderByRelationshipId(2);
        assertNotNull(reminders);
        assertEquals(1, reminders.size());
    }

    @Test
    void shouldNotFindAllRemindersByInvalidRelationshipId() {
        List<Reminder> reminders = repository.findAllReminderByRelationshipId(99);
        assertEquals(0, reminders.size());
    }

    @Test
    void shouldFindReminderById() {
        Reminder reminder = repository.findReminderById(1);
        assertNotNull(reminder);

        assertEquals(1, reminder.getReminderId());
        assertEquals("Birthday", reminder.getName());
        assertEquals(LocalDate.of(2024, 1, 4), reminder.getDate());
        assertEquals("It's your birthday today.", reminder.getDescription());
        assertEquals(1, reminder.getUserId());
        assertEquals(0, reminder.getRelationshipId());
        assertEquals(0, reminder.getNotableDayId());
    }

    @Test
    void shouldNotFindReminderByInvalidId() {
        Reminder reminder = repository.findReminderById(99);
        assertNull(reminder);
    }

    @Test
    void shouldAdd() {
        Reminder reminder = makeReminder();
        Reminder actual = repository.add(reminder);

        assertEquals(4, actual.getReminderId());
        assertEquals("Valentines Day", actual.getName());
        assertEquals(LocalDate.now().plusDays(15), actual.getDate());
        assertEquals("First Valentines Day!", actual.getDescription());
        assertEquals(2, actual.getUserId());
        assertEquals(2, actual.getRelationshipId());
        assertEquals(1, actual.getNotableDayId());
    }

    @Test
    void shouldAddWithNullFk() {
        Reminder reminder = makeReminder();
        reminder.setRelationshipId(0);
        reminder.setNotableDayId(0);
        Reminder actual = repository.add(reminder);

        assertEquals(5, actual.getReminderId());
        assertEquals("Valentines Day", actual.getName());
        assertEquals(LocalDate.now().plusDays(15), actual.getDate());
        assertEquals("First Valentines Day!", actual.getDescription());
        assertEquals(2, actual.getUserId());
        assertEquals(0, actual.getRelationshipId());
        assertEquals(0, actual.getNotableDayId());
    }

    @Test
    void shouldUpdate() {
        Reminder reminder = repository.findReminderById(2);
        assertNotNull(reminder);

        reminder.setName("TEST DAY!");
        reminder.setDescription("TEST DESCRIPTION!!");
        reminder.setRelationshipId(1);

        boolean actual = repository.update(reminder);
        assertTrue(actual);
    }

    @Test
    void shouldDelete() {
        boolean actual = repository.delete(3);
        assertTrue(actual);
    }

    private Reminder makeReminder() {
        Reminder reminder = new Reminder();

        reminder.setName("Valentines Day");
        reminder.setDate(LocalDate.now().plusDays(15));
        reminder.setDescription("First Valentines Day!");
        reminder.setUserId(2);
        reminder.setRelationshipId(2);
        reminder.setNotableDayId(1);

        return reminder;
    }
}