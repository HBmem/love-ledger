package love.loveledger.domain;

import love.loveledger.data.MilestoneRepository;
import love.loveledger.models.Milestone;
import love.loveledger.models.MilestoneType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MilestoneServiceTest {
    @Autowired
    MilestoneService service;

    @MockBean
    MilestoneRepository milestoneRepository;

    @Test
    void shouldAdd() {
        Milestone milestone = makeMilestone();

        Milestone mockOut = makeMilestone();
        mockOut.setMilestoneId(4);

        when(milestoneRepository.add(milestone)).thenReturn(mockOut);

        Result<Milestone> actual = service.add(milestone);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotAddWhenInvalid() {
        Milestone milestone = makeMilestone();

        // Should not add when id is not 0
        milestone.setMilestoneId(99);
        Result<Milestone> actual = service.add(milestone);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when name is empty
        milestone = makeMilestone();
        milestone.setName("");
        actual = service.add(milestone);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when name is null
        milestone.setName(null);
        actual = service.add(milestone);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when name is not unique
        milestone.setName("First Valentine");

        when(milestoneRepository.findAllMilestone()).thenReturn(makeMilestoneList());

        actual = service.add(milestone);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when description is empty
        milestone = makeMilestone();
        milestone.setDescription("");
        actual = service.add(milestone);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when description is null
        milestone.setDescription(null);
        actual = service.add(milestone);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when milestone type is null
        milestone = makeMilestone();
        milestone.setType(null);
        actual = service.add(milestone);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldUpdate() {
        Milestone milestone = makeMilestone();
        milestone.setMilestoneId(4);

        when(milestoneRepository.update(milestone)).thenReturn(true);
        Result<Milestone> actual = service.update(milestone);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateMissing() {
        Milestone milestone = makeMilestone();
        milestone.setMilestoneId(99);

        when(milestoneRepository.update(milestone)).thenReturn(false);
        Result<Milestone> actual = service.update(milestone);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
    }

    @Test
    void shouldNotUpdateWhenInvalid() {
        Milestone milestone = makeMilestone();
        milestone.setMilestoneId(3);

        // Should not add when name is empty
        milestone = makeMilestone();
        milestone.setName("");
        Result<Milestone> actual = service.add(milestone);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when name is null
        milestone.setName(null);
        actual = service.update(milestone);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when name is not unique
        milestone.setName("First Valentine");

        when(milestoneRepository.findAllMilestone()).thenReturn(makeMilestoneList());

        actual = service.update(milestone);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when description is empty
        milestone = makeMilestone();
        milestone.setDescription("");
        actual = service.update(milestone);
        assertEquals(ResultType.INVALID, actual.getType());

        //Should not add when description is null
        milestone.setDescription(null);
        actual = service.update(milestone);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when milestone type is null
        milestone = makeMilestone();
        milestone.setType(null);
        actual = service.update(milestone);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    private Milestone makeMilestone() {
        Milestone milestone = new Milestone();

        milestone.setName("First Christmas");
        milestone.setDescription("We celebrated our first christmas together!");
        milestone.setType(MilestoneType.RELATIONSHIP);

        return milestone;
    }

    private List<Milestone> makeMilestoneList() {
        List<Milestone> milestones = new ArrayList<>();

        Milestone milestone1 = new Milestone();

        milestone1.setName("First Christmas");
        milestone1.setDescription("We celebrated our first christmas together!");
        milestone1.setType(MilestoneType.RELATIONSHIP);
        milestones.add(milestone1);

        Milestone milestone2 = new Milestone();

        milestone2.setName("First Valentine");
        milestone2.setDescription("We celebrated our first valentine!");
        milestone2.setType(MilestoneType.RELATIONSHIP);
        milestones.add(milestone2);

        return milestones;
    }
}