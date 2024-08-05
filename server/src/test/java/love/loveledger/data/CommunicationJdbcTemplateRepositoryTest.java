package love.loveledger.data;

import love.loveledger.models.Communication;
import love.loveledger.models.CommunicationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CommunicationJdbcTemplateRepositoryTest {
    @Autowired
    CommunicationJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() { knownGoodState.set(); }

    @Test
    void shouldFindAllCommunicationByRelationshipId() {
        List<Communication> communications = repository.findAllCommunicationByRelationshipId(1);
        assertNotNull(communications);
        assertEquals(2, communications.size());
    }

    @Test
    void shouldNotFindCommunicationByInvalidRelationshipId() {
        List<Communication> communications = repository.findAllCommunicationByRelationshipId(99);
        assertEquals(0, communications.size());
    }

    @Test
    void shouldFindCommunicationById() {
        Communication communication = repository.findCommunicationById(1);
        assertNotNull(communication);

        assertEquals(1, communication.getCommunicationId());
        assertEquals(LocalDate.of(2024, 6, 10), communication.getDate());
        assertEquals(CommunicationType.TEXT, communication.getType());
        assertEquals("Talked about fighting", communication.getDescription());
        assertEquals(4, communication.getMoodScore());
        assertEquals(1, communication.getRelationshipId());
    }

    @Test
    void shouldNotFindCommunicationByInvalidId() {
        Communication communication = repository.findCommunicationById(99);
        assertNull(communication);
    }

    @Test
    void shouldAdd() {
        Communication communication = makeCommunication();
        Communication actual = repository.add(communication);

        assertEquals(4, actual.getCommunicationId());
        assertEquals(LocalDate.of(2024, 1, 12), actual.getDate());
        assertEquals("Spoke about the 2024 election candidates", actual.getDescription());
        assertEquals(2, actual.getMoodScore());
        assertEquals(2, actual.getRelationshipId());
    }

    @Test
    void shouldUpdate() {
        Communication communication = repository.findCommunicationById(2);
        assertNotNull(communication);

        communication.setMoodScore(1);
        communication.setDate(LocalDate.of(2024, 5, 10));

        boolean actual = repository.update(communication);
        assertTrue(actual);
    }

    @Test
    void shouldDelete() {
        boolean actual = repository.delete(3);
        assertTrue(actual);
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