package love.loveledger.data;

import love.loveledger.models.Gender;
import love.loveledger.models.LoveInterest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class LoveInterestJdbcTemplateRepositoryTest {

    final static int NEXT_ID = 1;

    @Autowired
    LoveInterestJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAllLoveInterestByUserId() {
        List<LoveInterest> loveInterests = repository.findAllLoveInterestByUserId(1);
        assertNotNull(loveInterests);
        assertEquals(1, loveInterests.size());
    }

    @Test
    void shouldNotFindAllLoveInterestByInvalidUserId() {
        List<LoveInterest> loveInterests = repository.findAllLoveInterestByUserId(99);
        assertEquals(0, loveInterests.size());
    }

    @Test
    void shouldFindLoveInterestById() {
        LoveInterest loveInterest = repository.findLoveInterestById(1);
        assertNotNull(loveInterest);

        assertEquals("Princess", loveInterest.getNickname());
        assertEquals("Fiona", loveInterest.getFirstName());
        assertEquals("Ogre", loveInterest.getLastName());
        assertEquals(Gender.FEMALE, loveInterest.getGender());
        assertEquals(LocalDate.of(1996, 4, 28), loveInterest.getBirthday());
        assertEquals("fighting;singing;dancing", loveInterest.getHobbiesString());
        assertEquals("color green;birds;fights", loveInterest.getLikesString());
        assertEquals("short people;color pink", loveInterest.getDislikesString());
        assertEquals(1, loveInterest.getUserId());
    }

    @Test
    void shouldNotFindLoveInterestByInvalidId() {
        LoveInterest loveInterest = repository.findLoveInterestById(99);
        assertNull(loveInterest);
    }

    @Test
    void shouldAdd() {
        LoveInterest loveInterest = makeLoveInterest();
        LoveInterest actual = repository.add(loveInterest);

        assertEquals(4, actual.getLoveInterestId());
        assertNull(actual.getNickname());
        assertEquals("Donald", actual.getFirstName());
        assertEquals("Trump", actual.getLastName());
        assertEquals(Gender.MALE, actual.getGender());
        assertEquals(LocalDate.of(1996, 6, 14), actual.getBirthday());
        assertEquals("lying;doing crimes;getting arrested", actual.getHobbiesString());
        assertEquals("lies;crimes;fascism", actual.getLikesString());
        assertEquals("honesty;democracy", actual.getDislikesString());
        assertEquals(2, actual.getUserId());
    }

    @Test
    void shouldUpdate() {
        LoveInterest loveInterest = repository.findLoveInterestById(2);
        assertNotNull(loveInterest);

        loveInterest.setLastName("Pecker");
        loveInterest.setBirthday(LocalDate.of(1999, 12, 11));

        boolean actual = repository.update(loveInterest);
        assertTrue(actual);
    }

    @Test
    void shouldDelete() {
        boolean actual = repository.delete(3);
        assertTrue(actual);
    }

    private LoveInterest makeLoveInterest() {
        LoveInterest loveInterest = new LoveInterest();

        loveInterest.setFirstName("Donald");
        loveInterest.setLastName("Trump");
        loveInterest.setGender(Gender.MALE);
        loveInterest.setBirthday(LocalDate.of(1996, 6, 14));
        loveInterest.setHobbies("lying;doing crimes;getting arrested");
        loveInterest.setLikes("lies;crimes;fascism");
        loveInterest.setDislikes("honesty;democracy");
        loveInterest.setUserId(2);

        return loveInterest;
    }
}