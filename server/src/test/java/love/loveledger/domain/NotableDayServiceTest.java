package love.loveledger.domain;

import love.loveledger.data.NotableDayRepository;
import love.loveledger.models.NotableDay;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class NotableDayServiceTest {
    @Autowired
    NotableDayService service;

    @MockBean
    NotableDayRepository notableDayRepository;

    @Test
    void shouldAdd() {
        NotableDay notableDay = makeNotableDay();

        NotableDay mockOut = makeNotableDay();
        mockOut.setNotableDayId(3);

        when(notableDayRepository.add(notableDay)).thenReturn(mockOut);

        Result<NotableDay> actual = service.add(notableDay);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotAddWhenInvalid() {
        NotableDay notableDay = makeNotableDay();

        // Should not add when id is not 0
        notableDay.setNotableDayId(99);
        Result<NotableDay> actual = service.add(notableDay);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when name is blank or null
        notableDay.setNotableDayId(0);
        notableDay.setName(null);
        actual = service.add(notableDay);
        assertEquals(ResultType.INVALID, actual.getType());

        notableDay.setName("");
        actual = service.add(notableDay);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when day is less than 1 or greater than 31
        notableDay = makeNotableDay();
        notableDay.setDay(0);
        actual = service.add(notableDay);
        assertEquals(ResultType.INVALID, actual.getType());

        notableDay.setDay(32);
        actual = service.add(notableDay);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when month is less than 1 or greater than 12
        notableDay = makeNotableDay();
        notableDay.setMonth(0);
        actual = service.add(notableDay);
        assertEquals(ResultType.INVALID, actual.getType());

        notableDay.setMonth(13);
        actual = service.add(notableDay);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldUpdate() {
        NotableDay notableDay = makeNotableDay();
        notableDay.setNotableDayId(4);

        when(notableDayRepository.update(notableDay)).thenReturn(true);

        Result<NotableDay> actual = service.update(notableDay);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateWhenMissing() {
        NotableDay notableDay = makeNotableDay();
        notableDay.setNotableDayId(99);

        when(notableDayRepository.update(notableDay)).thenReturn(false);

        Result<NotableDay> actual = service.update(notableDay);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
    }

    @Test
    void shouldNotUpdateWhenInvalid() {
        NotableDay notableDay = makeNotableDay();
        notableDay.setNotableDayId(3);

        // Should not update when name is blank or null
        notableDay.setNotableDayId(0);
        notableDay.setName(null);
        Result<NotableDay> actual = service.update(notableDay);
        assertEquals(ResultType.INVALID, actual.getType());

        notableDay.setName("");
        actual = service.update(notableDay);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when day is less than 1 or greater than 31
        notableDay = makeNotableDay();
        notableDay.setDay(0);
        actual = service.update(notableDay);
        assertEquals(ResultType.INVALID, actual.getType());

        notableDay.setDay(32);
        actual = service.update(notableDay);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when month is less than 1 or greater than 12
        notableDay = makeNotableDay();
        notableDay.setMonth(0);
        actual = service.update(notableDay);
        assertEquals(ResultType.INVALID, actual.getType());

        notableDay.setMonth(13);
        actual = service.update(notableDay);
        assertEquals(ResultType.INVALID, actual.getType());
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