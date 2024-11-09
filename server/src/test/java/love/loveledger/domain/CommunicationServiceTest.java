package love.loveledger.domain;

import love.loveledger.data.CommunicationRepository;
import love.loveledger.data.RelationshipRepository;
import love.loveledger.models.Communication;
import love.loveledger.models.CommunicationType;
import love.loveledger.models.Relationship;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CommunicationServiceTest {
    @Autowired
    CommunicationService service;

    @MockBean
    CommunicationRepository communicationRepository;
    @MockBean
    RelationshipRepository relationshipRepository;

    @Test
    void shouldAdd() {
        Communication communication = makeCommunication();

        Communication mockOut = makeCommunication();
        mockOut.setCommunicationId(4);

        when(communicationRepository.add(communication)).thenReturn(mockOut);
        when(relationshipRepository.findRelationshipById(communication.getRelationshipId())).thenReturn(makeRelationship());

        Result<Communication> actual = service.add(communication);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotAddWhenInvalid() {
        Communication communication = makeCommunication();

        // Should not add when id is not 0
        communication.setCommunicationId(99);
        Result<Communication> actual = service.add(communication);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when type is null
        communication = makeCommunication();
        communication.setType(null);
        actual = service.add(communication);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when date is null
        communication = makeCommunication();
        communication.setDate(null);
        actual = service.add(communication);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when mood score is less than 1
        communication = makeCommunication();
        communication.setMoodScore(0);
        actual = service.add(communication);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when mood score is greater than 5
        communication = makeCommunication();
        communication.setMoodScore(9);
        actual = service.add(communication);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when relationship does not exist
        communication = makeCommunication();
        communication.setRelationshipId(99);

        when(relationshipRepository.findRelationshipById(communication.getRelationshipId())).thenReturn(null);
        actual = service.add(communication);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldUpdate() {
        Communication communication = makeCommunication();
        communication.setCommunicationId(4);

        when(communicationRepository.update(communication)).thenReturn(true);
        when(relationshipRepository.findRelationshipById(communication.getRelationshipId())).thenReturn(makeRelationship());

        Result<Communication> actual = service.update(communication);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateMissing() {
        Communication communication = makeCommunication();
        communication.setCommunicationId(99);

        when(communicationRepository.update(communication)).thenReturn(false);
        when(relationshipRepository.findRelationshipById(communication.getRelationshipId())).thenReturn(makeRelationship());

        Result<Communication> actual = service.update(communication);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
    }

    @Test
    void shouldNotUpdateWhenInvalid() {
        Communication communication = makeCommunication();
        communication.setCommunicationId(3);

        // Should not update when type is null
        communication.setType(null);
        Result<Communication> actual = service.update(communication);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when date is null
        communication = makeCommunication();
        communication.setDate(null);
        actual = service.update(communication);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when mood score is less than 1
        communication = makeCommunication();
        communication.setMoodScore(0);
        actual = service.update(communication);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when mood score is greater than 5
        communication = makeCommunication();
        communication.setMoodScore(9);
        actual = service.update(communication);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when relationship does not exist
        communication = makeCommunication();
        communication.setRelationshipId(99);

        when(relationshipRepository.findRelationshipById(communication.getRelationshipId())).thenReturn(null);
        actual = service.update(communication);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    private Communication makeCommunication() {
        Communication communication = new Communication();

        communication.setDate(LocalDate.of(2024, 1, 12));
        communication.setType(CommunicationType.EMAIL);
        communication.setDescription("Spoke about the 2024 election candidates");
        communication.setMoodScore(2);
        communication.setRelationshipId(2);

        return communication;
    }

    private Relationship makeRelationship() {
        Relationship relationship = new Relationship();

        relationship.setRelationshipId(2);

        return relationship;
    }
}