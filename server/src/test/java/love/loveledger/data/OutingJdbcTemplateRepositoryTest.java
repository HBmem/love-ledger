package love.loveledger.data;

import love.loveledger.models.Outing;
import love.loveledger.models.OutingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class OutingJdbcTemplateRepositoryTest {
    @Autowired
    OutingJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() { knownGoodState.set(); }

    @Test
    void shouldFindAllOutingByRelationshipId() {
        List<Outing> outings = repository.findAllOutingsByRelationshipId(1);
        assertNotNull(outings);
        assertEquals(2, outings.size());
    }

    @Test
    void shouldNotFindOutingByInvalidRelationshipId() {
        List<Outing> outings = repository.findAllOutingsByRelationshipId(99);
        assertEquals(0, outings.size());
    }

    @Test
    void shouldFindByOutingId() {
        Outing outing = repository.findOutingById(1);
        assertNotNull(outing);

        assertEquals("Feb 1st Park Date", outing.getName());
        assertEquals(OutingType.DATE, outing.getType());
        assertEquals("Went to the park with my date. We talked for a while then took a walk together.", outing.getDescription());
        assertEquals("Overton Park", outing.getLocation());
        assertEquals("The date was fun", outing.getOutcome());
        assertEquals(LocalDateTime.of(2024, 1, 31, 21, 0, 0), outing.getStartTime());
        assertEquals(LocalDateTime.of(2024, 1, 31, 22, 0, 0), outing.getEndTime());
        assertEquals(1, outing.getRelationshipId());
    }

    @Test
    void shouldNotFindOutingByInvalidId() {
        Outing outing = repository.findOutingById(99);
        assertNull(outing);
    }

    @Test
    void shouldAdd() {
        Outing outing = makeOuting();
        Outing actual = repository.add(outing);

        assertEquals(4, actual.getOutingId());
        assertEquals("Sneaky Link", actual.getName());
        assertEquals(OutingType.HOUSE_VISIT, actual.getType());
        assertEquals("Went to their house to link ;)", actual.getDescription());
        assertEquals("Crushes House", actual.getLocation());
        assertEquals("It was GREAT!!! they didn't seem happy tho", actual.getOutcome());
        assertEquals(LocalDateTime.of(2024, 2, 1, 3, 0, 0), actual.getStartTime());
        assertEquals(LocalDateTime.of(2024, 2, 1, 3, 1, 0), actual.getEndTime());
        assertEquals(2, actual.getRelationshipId());
    }

    @Test
    void shouldUpdate() {
        Outing outing = repository.findOutingById(2);
        assertNotNull(outing);

        outing.setName("HORRIBLE Sneaky Link");
        outing.setOutcome("They broke up with me :(");

        boolean actual = repository.update(outing);
        assertTrue(actual);
    }

    @Test
    void shouldDelete() {
        boolean actual = repository.delete(3);
        assertTrue(actual);
    }

    private Outing makeOuting() {
        Outing outing = new Outing();

        outing.setName("Sneaky Link");
        outing.setType(OutingType.HOUSE_VISIT);
        outing.setDescription("Went to their house to link ;)");
        outing.setLocation("Crushes House");
        outing.setOutcome("It was GREAT!!! they didn't seem happy tho");
        outing.setStartTime(LocalDateTime.of(2024, 2, 1, 3, 0, 0));
        outing.setEndTime(LocalDateTime.of(2024, 2, 1, 3, 1, 0));
        outing.setRelationshipId(2);

        return outing;
    }
}