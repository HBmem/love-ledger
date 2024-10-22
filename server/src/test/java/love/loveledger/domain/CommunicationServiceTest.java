package love.loveledger.domain;

import love.loveledger.data.CommunicationRepository;
import love.loveledger.models.Communication;
import love.loveledger.models.CommunicationType;
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

    @Test
    void shouldAdd() {
        Communication communication = makeCommunication();

        Communication mockOut = makeCommunication();
        mockOut.setCommunicationId(4);

        when(communicationRepository.add(communication)).thenReturn(mockOut);

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
    }

    @Test
    void shouldUpdate() {
        Communication communication = makeCommunication();
        communication.setCommunicationId(4);

        when(communicationRepository.update(communication)).thenReturn(true);
        Result<Communication> actual = service.update(communication);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateMissing() {
        Communication communication = makeCommunication();
        communication.setCommunicationId(99);

        when(communicationRepository.update(communication)).thenReturn(false);
        Result<Communication> actual = service.update(communication);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
    }

    @Test
    void shouldNotUpdateWhenInvalid() {
        Communication communication = makeCommunication();
        communication.setCommunicationId(3);

        // Should not add when type is null
        communication.setType(null);
        Result<Communication> actual = service.update(communication);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when date is null
        communication = makeCommunication();
        communication.setDate(null);
        actual = service.update(communication);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when mood score is less than 1
        communication = makeCommunication();
        communication.setMoodScore(0);
        actual = service.update(communication);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when mood score is greater than 5
        communication = makeCommunication();
        communication.setMoodScore(9);
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
}