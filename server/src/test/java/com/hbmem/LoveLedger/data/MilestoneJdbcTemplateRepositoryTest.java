package com.hbmem.LoveLedger.data;

import com.hbmem.LoveLedger.model.Milestone;
import com.hbmem.LoveLedger.model.MilestoneType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.NONE)
class MilestoneJdbcTemplateRepositoryTest {
    @Autowired
    MilestoneRepository repository;
    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindById() {
        Milestone milestone = repository.findById(1);
        assertNotNull(milestone);
        assertEquals(1, milestone.getId());
        assertEquals("Six Month Anniversary", milestone.getName());
        assertEquals("Your relationship has lasted 6 months", milestone.getDescription());
        assertEquals("{\"type\": \"RELATIONSHIP\", \"condition\": {\"duration\": \"6 months\"}}", milestone.getCondition());
    }

    @Test
    void shouldFindAll() {
        List<Milestone> milestones = repository.findAllMilestones();
        assertNotNull(milestones);
        assertTrue(milestones.size() >= 2);
    }

    @Test
    void shouldAdd() {
        Milestone milestone = makeMilestone();
        Milestone actual = repository.add(milestone);
        assertNotNull(actual);
        assertEquals(4, actual.getId());
    }

    @Test
    void shouldUpdate() {
        Milestone milestone = repository.findById(2);
        assertNotNull(milestone);

        milestone.setName("Test Milestone");
        boolean actual = repository.update(milestone);
        assertTrue(actual);
    }

    @Test
    void delete() {
        boolean actual = repository.delete(3);
        assertTrue(actual);
    }

    private Milestone makeMilestone() {
        Milestone milestone = new Milestone();

        milestone.setName("First Christmas");
        milestone.setDescription("We celebrated our first christmas together!");
        milestone.setType(MilestoneType.RELATIONSHIP);
        milestone.setCondition("{\"type\": \"RELATIONSHIP\", \"condition\": {\"event\": \"Christmas\"}}");

        return milestone;
    }
}