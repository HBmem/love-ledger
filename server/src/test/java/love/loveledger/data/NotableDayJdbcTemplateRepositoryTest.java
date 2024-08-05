package love.loveledger.data;

import love.loveledger.models.NotableDay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class NotableDayJdbcTemplateRepositoryTest {
    @Autowired
    NotableDayJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() { knownGoodState.set(); }

    @Test
    void shouldFindAll() {
        List<NotableDay> notableDays = repository.findAll();
        assertTrue(notableDays.size() >= 2);
    }

    @Test
    void shouldFindNotableDayById() {
        NotableDay notableDay = repository.findById(1);
        assertNotNull(notableDay);

        assertEquals(1, notableDay.getNotableDayId());
        assertEquals("Valentines Day", notableDay.getName());
        assertEquals("A day of love celebrated around the world.", notableDay.getDescription());
        assertEquals(14,notableDay.getDay());
        assertEquals(2, notableDay.getMonth());
        assertEquals(-1, notableDay.getYear());
    }

    @Test
    void shouldNotFindNotableDayByInvalidId() {
        NotableDay notableDay = repository.findById(99);
        assertNull(notableDay);
    }

    @Test
    void shouldAdd() {
        NotableDay notableDay = makeNotableDay();
        NotableDay actual = repository.add(notableDay);

        assertEquals(4, actual.getNotableDayId());
        assertEquals("Thanksgiving 2023", actual.getName());
        assertEquals("Time to meet the family.", actual.getDescription());
        assertEquals(23,actual.getDay());
        assertEquals(11, actual.getMonth());
        assertEquals(2023, actual.getYear());
    }

    @Test
    void shouldUpdate() {
        NotableDay notableDay = repository.findById(2);
        assertNotNull(notableDay);

        notableDay.setDescription("TEST DESCRIPTION!!");
        notableDay.setYear(2024);

        boolean actual = repository.update(notableDay);
        assertTrue(actual);
    }

    @Test
    void shouldDelete() {
        boolean actual = repository.delete(3);
        assertTrue(actual);
    }

    private NotableDay makeNotableDay() {
        NotableDay notableDay = new NotableDay();

        notableDay.setName("Thanksgiving 2023");
        notableDay.setDescription("Time to meet the family.");
        notableDay.setDay(23);
        notableDay.setMonth(11);
        notableDay.setYear(2023);

        return notableDay;
    }
}