package love.loveledger.data;

import love.loveledger.models.Relationship;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RelationshipJdbcTemplateRepositoryTest {
    @Autowired
    RelationshipJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAllRelationshipByUserId() {
        List<Relationship> relationships = repository.findAllRelationshipByUserId(2);
        assertNotNull(relationships);
        assertEquals(2, relationships.size());
    }

    @Test
    void shouldNotFindRelationshipByInvalidUserId() {
        List<Relationship> relationships = repository.findAllRelationshipByUserId(99);
        assertEquals(0, relationships.size());
    }

    @Test
    void shouldFindByRelationshipId() {
        Relationship relationship = repository.findRelationshipById(1);
        assertNotNull(relationship);

        assertEquals(LocalDate.of(2024, 5, 1), relationship.getStartDate());
        assertNull(relationship.getEndDate());
        assertTrue(relationship.isOfficial());
        assertEquals("", relationship.getLabelsString());
        assertEquals(2, relationship.getUserId());
        assertEquals(2, relationship.getLoveInterestId());
        assertEquals(1, relationship.getRelationshipId());
    }

    @Test
    void shouldNotFindRelationshipByInvalidId() {
        Relationship relationship = repository.findRelationshipById(99);
        assertNull(relationship);
    }

    @Test
    void shouldAdd() {
        Relationship relationship = makeRelationship();
        Relationship actual = repository.add(relationship);

        assertEquals(4, actual.getRelationshipId());
        assertEquals(LocalDate.of(2023,1,12), actual.getStartDate());
        assertEquals(LocalDate.of(2023,11,27), actual.getEndDate());
        assertFalse(actual.isOfficial());
        assertEquals("", actual.getLabelsString());
        assertEquals(1, actual.getUserId());
        assertEquals(1, actual.getLoveInterestId());
    }

    @Test
    void shouldUpdate() {
        Relationship relationship = repository.findRelationshipById(2);
        assertNotNull(relationship);

        relationship.setLabels("Marriage");
        relationship.setStartDate(LocalDate.of(2022,8,13));

        boolean actual = repository.update(relationship);
        assertTrue(actual);
    }

    @Test
    void shouldDelete() {
        boolean actual = repository.delete(3);
        assertTrue(actual);
    }

    private Relationship makeRelationship() {
        Relationship relationship = new Relationship();

        relationship.setStartDate(LocalDate.of(2023,1,12));
        relationship.setEndDate(LocalDate.of(2023,11,27));
        relationship.setOfficial(false);
        relationship.setLabels("");
        relationship.setUserId(1);
        relationship.setLoveInterestId(1);

        return relationship;
    }
}