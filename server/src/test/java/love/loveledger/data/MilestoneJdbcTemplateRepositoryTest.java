package love.loveledger.data;

import love.loveledger.models.Milestone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MilestoneJdbcTemplateRepositoryTest {
    @Autowired
    MilestoneJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() { knownGoodState.set(); }

    @Test
    void shouldFindAllMilestone() {
        List<Milestone> milestones = repository.findAllMilestone();
        assertNotNull(milestones);
        assertEquals(3, milestones.size());
    }

    @Test
    void shouldFindMilestoneById() {
        Milestone milestone = repository.findMilestoneById(1);
        assertNotNull(milestone);

        assertEquals(1, milestone.getMilestoneId());
        assertEquals("New Relationship", milestone.getName());
        assertEquals("The start of something new hopefully this one goes somewhere.", milestone.getDescription());
    }

    @Test
    void shouldNotFindMilestoneByInvalidId() {
        Milestone milestone = repository.findMilestoneById(99);
        assertNull(milestone);
    }

    @Test
    void shouldAdd() {
        Milestone milestone = makeMilestone();
        Milestone actual = repository.add(milestone);

        assertEquals(4, actual.getMilestoneId());
        assertEquals("First Christmas", actual.getName());
        assertEquals("We celebrated our first christmas together!", actual.getDescription());
    }

    @Test
    void shouldUpdate() {
        Milestone milestone = repository.findMilestoneById(2);
        assertNotNull(milestone);

        milestone.setName("TEST");
        milestone.setDescription("TEST DESCRIPTION!!");

        boolean actual = repository.update(milestone);
        assertTrue(actual);
    }

    @Test
    void shouldDelete() {
        boolean actual = repository.delete(3);
        assertTrue(actual);
    }

    private Milestone makeMilestone() {
        Milestone milestone = new Milestone();

        milestone.setName("First Christmas");
        milestone.setDescription("We celebrated our first christmas together!");

        return milestone;
    }
}