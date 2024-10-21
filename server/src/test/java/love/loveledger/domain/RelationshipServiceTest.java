package love.loveledger.domain;

import love.loveledger.data.RelationshipRepository;
import love.loveledger.models.Relationship;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RelationshipServiceTest {
    @Autowired
    RelationshipService service;

    @MockBean
    RelationshipRepository relationshipRepository;

    @Test
    void shouldAdd() {
        Relationship relationship = makeRelationship();

        Relationship mockOut = makeRelationship();
        mockOut.setRelationshipId(4);

        when(relationshipRepository.add(relationship)).thenReturn(mockOut);

        Result<Relationship> actual = service.add(relationship);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }
    @Test
    void shouldNotAddWhenInvalid() {
        Relationship relationship = makeRelationship();

        // Should not add when id is not 0
        relationship.setRelationshipId(99);
        Result<Relationship> actual = service.add(relationship);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when start date is after end date
        relationship.setRelationshipId(0);
        relationship.setStartDate(LocalDate.of(2023,12,12));
        actual = service.add(relationship);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when start date is null
        relationship.setStartDate(null);
        actual = service.add(relationship);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when end date is before start date
        relationship.setStartDate(LocalDate.of(2023,1,12));
        relationship.setEndDate(LocalDate.of(2023,1,1));
        actual = service.add(relationship);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldUpdate() {
        Relationship relationship = makeRelationship();
        relationship.setRelationshipId(4);

        when(relationshipRepository.update(relationship)).thenReturn(true);
        Result<Relationship> actual = service.update(relationship);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateMissing() {
        Relationship relationship = makeRelationship();
        relationship.setRelationshipId(99);

        when(relationshipRepository.update(relationship)).thenReturn(false);
        Result<Relationship> actual = service.update(relationship);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
    }

    @Test
    void shouldNotUpdateWhenInvalid() {
        Relationship relationship = makeRelationship();
        relationship.setRelationshipId(3);

        // Should not update start date after end date
        relationship.setStartDate(LocalDate.of(2023,12,12));
        Result<Relationship> actual = service.update(relationship);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update null start date
        relationship.setStartDate(null);
        actual = service.update(relationship);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when end date is before start date
        relationship.setStartDate(LocalDate.of(2023,1,12));
        relationship.setEndDate(LocalDate.of(2023,1,1));
        actual = service.update(relationship);
        assertEquals(ResultType.INVALID, actual.getType());
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