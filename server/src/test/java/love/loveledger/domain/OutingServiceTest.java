package love.loveledger.domain;

import love.loveledger.data.OutingRepository;
import love.loveledger.data.RelationshipRepository;
import love.loveledger.models.Outing;
import love.loveledger.models.OutingType;
import love.loveledger.models.Relationship;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class OutingServiceTest {
    @Autowired
    OutingService service;

    @MockBean
    OutingRepository outingRepository;
    @MockBean
    RelationshipRepository relationshipRepository;

    @Test
    void shouldAdd() {
        Outing outing = makeOuting();

        Outing mockOut = makeOuting();
        mockOut.setOutingId(4);

        when(outingRepository.add(outing)).thenReturn(mockOut);
        when(relationshipRepository.findRelationshipById(outing.getRelationshipId())).thenReturn(makeRelationship());

        Result<Outing> actual = service.add(outing);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotAddWhenInvalid() {
        Outing outing = makeOuting();

        // Should not add when id is not 0
        outing.setOutingId(99);
        Result<Outing> actual = service.add(outing);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when end date is before start date
        outing.setOutingId(0);
        outing.setStartTime(LocalDateTime.of(2024, 12, 1, 13, 30));
        outing.setEndTime(LocalDateTime.of(2024, 12, 1, 13, 15));
        actual = service.add(outing);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when name is blank or null
        outing = makeOuting();
        outing.setName("");
        actual = service.add(outing);
        assertEquals(ResultType.INVALID, actual.getType());

        outing.setName(null);
        actual = service.add(outing);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when type is null
        outing = makeOuting();
        outing.setType(null);
        actual = service.add(outing);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when relationship id does not exist
        outing = makeOuting();
        outing.setRelationshipId(99);
        actual = service.add(outing);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldUpdate() {
        Outing outing = makeOuting();
        outing.setOutingId(3);

        when(outingRepository.update(outing)).thenReturn(true);
        when(relationshipRepository.findRelationshipById(outing.getRelationshipId())).thenReturn(makeRelationship());

        Result<Outing> actual = service.update(outing);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateMissing() {
        Outing outing = makeOuting();
        outing.setOutingId(99);

        when(outingRepository.update(outing)).thenReturn(false);
        when(relationshipRepository.findRelationshipById(outing.getRelationshipId())).thenReturn(makeRelationship());

        Result<Outing> actual = service.update(outing);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
    }

    @Test
    void shouldNotUpdateInvalid() {
        Outing outing = makeOuting();
        outing.setOutingId(3);

        // Should not update when end date is before start date
        outing.setOutingId(0);
        outing.setStartTime(LocalDateTime.of(2024, 12, 1, 13, 30));
        outing.setEndTime(LocalDateTime.of(2024, 12, 1, 13, 15));
        Result<Outing> actual = service.update(outing);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when name is blank or null
        outing = makeOuting();
        outing.setName("");
        actual = service.update(outing);
        assertEquals(ResultType.INVALID, actual.getType());

        outing.setName(null);
        actual = service.update(outing);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when type is null
        outing = makeOuting();
        outing.setType(null);
        actual = service.update(outing);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when relationship id does not exist
        outing = makeOuting();
        outing.setRelationshipId(99);
        actual = service.update(outing);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    private Outing makeOuting() {
        Outing outing = new Outing();

        outing.setName("Sneaky Link");
        outing.setType(OutingType.HOUSE_VISIT);
        outing.setDescription("Went to their house to link ;)");
        outing.setLocation("Crushes House");
        outing.setCost(BigDecimal.valueOf(100.50));
        outing.setRating(5);
        outing.setOutcome("It was GREAT!!! they didn't seem happy tho");
        outing.setStartTime(LocalDateTime.of(2024, 2, 1, 3, 0, 0));
        outing.setEndTime(LocalDateTime.of(2024, 2, 1, 3, 1, 0));
        outing.setRelationshipId(2);

        return outing;
    }
    
    private Relationship makeRelationship() {
        Relationship relationship = new Relationship();

        relationship.setRelationshipId(2);

        return relationship;
    }
}