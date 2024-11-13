package love.loveledger.domain;

import love.loveledger.data.NotableDayRepository;
import love.loveledger.data.RelationshipRepository;
import love.loveledger.data.ReminderRepository;
import love.loveledger.data.UserRepository;
import love.loveledger.models.AppUser;
import love.loveledger.models.NotableDay;
import love.loveledger.models.Relationship;
import love.loveledger.models.Reminder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ReminderServiceTest {
    @Autowired
    ReminderService service;

    @MockBean
    ReminderRepository reminderRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    RelationshipRepository relationshipRepository;
    @MockBean
    NotableDayRepository notableDayRepository;

    @Test
    void shouldAdd() {
        Reminder reminder = makeReminder();

        Reminder mockOut = makeReminder();
        mockOut.setReminderId(4);

        when(reminderRepository.add(reminder)).thenReturn(mockOut);
        when(userRepository.findByUserId(reminder.getUserId())).thenReturn(makeUser());
        when(relationshipRepository.findRelationshipById(reminder.getRelationshipId())).thenReturn(makeRelationship());
        when(notableDayRepository.findById(reminder.getNotableDayId())).thenReturn(makeNotableDay());

        Result<Reminder> actual = service.add(reminder);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotAddWhenInvalid() {
        Reminder reminder = makeReminder();

        // Should not add when id is not 0
        reminder.setReminderId(99);
        Result<Reminder> actual = service.add(reminder);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when name is null or blank
        reminder.setReminderId(0);
        reminder.setName(null);
        actual = service.add(reminder);
        assertEquals(ResultType.INVALID, actual.getType());

        reminder.setName("");
        actual = service.add(reminder);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when date is null
        reminder = makeReminder();
        reminder.setDate(null);
        actual = service.add(reminder);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when date has passed
        reminder.setDate(LocalDate.now().minusWeeks(2));
        actual = service.add(reminder);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when user does not exist
        reminder = makeReminder();
        when(userRepository.findByUserId(reminder.getUserId())).thenReturn(null);
        when(relationshipRepository.findRelationshipById(reminder.getRelationshipId())).thenReturn(makeRelationship());
        when(notableDayRepository.findById(reminder.getNotableDayId())).thenReturn(makeNotableDay());
        actual = service.add(reminder);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when relationship does not exist
        reminder = makeReminder();
        when(userRepository.findByUserId(reminder.getUserId())).thenReturn(makeUser());
        when(relationshipRepository.findRelationshipById(reminder.getRelationshipId())).thenReturn(null);
        when(notableDayRepository.findById(reminder.getNotableDayId())).thenReturn(makeNotableDay());
        actual = service.add(reminder);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when notable day does not exist
        reminder = makeReminder();
        when(userRepository.findByUserId(reminder.getUserId())).thenReturn(makeUser());
        when(relationshipRepository.findRelationshipById(reminder.getRelationshipId())).thenReturn(makeRelationship());
        when(notableDayRepository.findById(reminder.getNotableDayId())).thenReturn(null);
        actual = service.add(reminder);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldUpdate() {
        Reminder reminder = makeReminder();
        reminder.setReminderId(4);

        when(reminderRepository.update(reminder)).thenReturn(true);
        when(userRepository.findByUserId(reminder.getUserId())).thenReturn(makeUser());
        when(relationshipRepository.findRelationshipById(reminder.getRelationshipId())).thenReturn(makeRelationship());
        when(notableDayRepository.findById(reminder.getNotableDayId())).thenReturn(makeNotableDay());

        Result<Reminder> actual = service.update(reminder);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateMissing() {
        Reminder reminder = makeReminder();
        reminder.setReminderId(99);

        when(reminderRepository.update(reminder)).thenReturn(false);
        when(userRepository.findByUserId(reminder.getUserId())).thenReturn(makeUser());
        when(relationshipRepository.findRelationshipById(reminder.getRelationshipId())).thenReturn(makeRelationship());
        when(notableDayRepository.findById(reminder.getNotableDayId())).thenReturn(makeNotableDay());

        Result<Reminder> actual = service.update(reminder);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
    }

    @Test
    void shouldNotUpdateWhenInvalid() {
        Reminder reminder = makeReminder();
        reminder.setReminderId(3);

        // Should not update when name is null or blank
        reminder.setName(null);
        Result<Reminder> actual = service.update(reminder);
        assertEquals(ResultType.INVALID, actual.getType());

        reminder.setName("");
        actual = service.update(reminder);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when date is null
        reminder = makeReminder();
        reminder.setDate(null);
        actual = service.update(reminder);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when date has passed
        reminder.setDate(LocalDate.now().minusWeeks(2));
        actual = service.update(reminder);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when user does not exist
        reminder = makeReminder();
        when(userRepository.findByUserId(reminder.getUserId())).thenReturn(null);
        when(relationshipRepository.findRelationshipById(reminder.getRelationshipId())).thenReturn(makeRelationship());
        when(notableDayRepository.findById(reminder.getNotableDayId())).thenReturn(makeNotableDay());
        actual = service.update(reminder);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when relationship does not exist
        reminder = makeReminder();
        when(userRepository.findByUserId(reminder.getUserId())).thenReturn(makeUser());
        when(relationshipRepository.findRelationshipById(reminder.getRelationshipId())).thenReturn(null);
        when(notableDayRepository.findById(reminder.getNotableDayId())).thenReturn(makeNotableDay());
        actual = service.update(reminder);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when notable day does not exist
        reminder = makeReminder();
        when(userRepository.findByUserId(reminder.getUserId())).thenReturn(makeUser());
        when(relationshipRepository.findRelationshipById(reminder.getRelationshipId())).thenReturn(makeRelationship());
        when(notableDayRepository.findById(reminder.getNotableDayId())).thenReturn(null);
        actual = service.update(reminder);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    private Reminder makeReminder() {
        Reminder reminder = new Reminder();

        reminder.setName("Valentines Day");
        reminder.setDate(LocalDate.now().plusDays(15));
        reminder.setDescription("First Valentines Day!");
        reminder.setUserId(2);
        reminder.setRelationshipId(1);
        reminder.setNotableDayId(1);

        return reminder;
    }

    private AppUser makeUser() {
        AppUser user = new AppUser();

        user.setUserId(2);

        return user;
    }

    private Relationship makeRelationship() {
        Relationship relationship = new Relationship();

        relationship.setRelationshipId(1);

        return relationship;
    }

    private NotableDay makeNotableDay() {
        NotableDay notableDay = new NotableDay();

        notableDay.setNotableDayId(1);

        return notableDay;
    }
}