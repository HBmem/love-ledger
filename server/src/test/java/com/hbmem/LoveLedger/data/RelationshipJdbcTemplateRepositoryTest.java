package com.hbmem.LoveLedger.data;

import com.hbmem.LoveLedger.model.Relationship;
import com.hbmem.LoveLedger.model.RelationshipStatus;
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
    RelationshipRepository repository;
    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindById() {
        Relationship relationship = repository.findByRelationshipById(1);
        assertNotNull(relationship);
        assertEquals(1, relationship.getId());
        assertEquals(LocalDate.of(2023,2,12), relationship.getStartDate());
        assertNull(relationship.getEndDate());
        assertEquals(RelationshipStatus.ENGAGED, relationship.getRelationshipStatus());
        assertEquals(5, relationship.getImportanceLevel());
        assertTrue(relationship.isOfficial());
        assertEquals(2, relationship.getUserId());
        assertEquals(1, relationship.getLoveInterestId());
    }

    @Test
    void shouldFindAllRelationshipByUserId() {
        List<Relationship> relationships = repository.findAllRelationshipByUserId(1);
        assertNotNull(relationships);
        assertEquals(1, relationships.size());
    }

    @Test
    void shouldFindAllRelationshipByLoveInterestId() {
        List<Relationship> relationships = repository.findAllRelationshipByLoveInterestId(2);
        assertNotNull(relationships);
        assertEquals(1, relationships.size());
    }

    @Test
    void shouldAdd() {
        Relationship relationship = makeRelationship();
        Relationship actual = repository.add(relationship);
        assertNotNull(actual);
        assertEquals(4, actual.getId());
    }

    @Test
    void shouldUpdate() {
        Relationship relationship = repository.findByRelationshipById(2);
        assertNotNull(relationship);

        relationship.setEndDate(LocalDate.now().minusWeeks(8));

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
        relationship.setRelationshipStatus(RelationshipStatus.ENGAGED);
        relationship.setImportanceLevel(4);
        relationship.setOfficial(false);
        relationship.setUserId(1);
        relationship.setLoveInterestId(1);

        return relationship;
    }
}