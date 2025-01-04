package com.hbmem.LoveLedger.domain;

import com.hbmem.LoveLedger.data.LoveInterestRepository;
import com.hbmem.LoveLedger.data.RelationshipRepository;
import com.hbmem.LoveLedger.data.UserCredentialRepository;
import com.hbmem.LoveLedger.model.LoveInterest;
import com.hbmem.LoveLedger.model.Relationship;
import com.hbmem.LoveLedger.model.RelationshipStatus;
import com.hbmem.LoveLedger.model.UserCredential;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RelationshipServiceTest {
    @Autowired
    RelationshipService service;

    @MockitoBean
    RelationshipRepository relationshipRepository;
    @MockitoBean
    UserCredentialRepository userCredentialRepository;
    @MockitoBean
    LoveInterestRepository loveInterestRepository;

    @Test
    void shouldAdd() {
        Relationship relationship = makeRelationship();

        Relationship mockOut = makeRelationship();
        mockOut.setId(4);

        when(relationshipRepository.add(relationship)).thenReturn(mockOut);
        when(userCredentialRepository.findByUserId(relationship.getUserId())).thenReturn(makeUser());
        when(loveInterestRepository.findLoveInterestById(relationship.getLoveInterestId())).thenReturn(makeLoveInterest());

        Result<Relationship> actual = service.add(relationship);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotAddWhenInvalid() {
        Relationship relationship = makeRelationship();

        // Should not add when relationship is null
        Result<Relationship> actual = service.add(null);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when id is not 0
        relationship.setId(99);
        actual = service.add(relationship);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when start date is null
        relationship = makeRelationship();
        relationship.setStartDate(null);
        actual = service.add(relationship);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when end date is before start date
        relationship = makeRelationship();
        relationship.setStartDate(LocalDate.now());
        relationship.setEndDate(LocalDate.now().minusWeeks(1));
        actual = service.add(relationship);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when relationship status is null
        relationship = makeRelationship();
        relationship.setRelationshipStatus(null);
        actual = service.add(relationship);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when importance level is not between 1 and 5
        relationship = makeRelationship();
        relationship.setImportanceLevel(0);
        actual = service.add(relationship);
        assertEquals(ResultType.INVALID, actual.getType());

        relationship.setImportanceLevel(6);
        actual = service.add(relationship);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when user does not exist
        relationship = makeRelationship();
        relationship.setUserId(99);

        when(userCredentialRepository.findByUserId(relationship.getUserId())).thenReturn(null);
        when(loveInterestRepository.findLoveInterestById(relationship.getLoveInterestId())).thenReturn(makeLoveInterest());

        actual = service.add(relationship);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when love interest does not exist
        relationship = makeRelationship();
        relationship.setLoveInterestId(99);

        when(userCredentialRepository.findByUserId(relationship.getUserId())).thenReturn(makeUser());
        when(loveInterestRepository.findLoveInterestById(relationship.getLoveInterestId())).thenReturn(null);

        actual = service.add(relationship);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldUpdate() {
        Relationship relationship = makeRelationship();
        relationship.setId(4);

        when(relationshipRepository.update(relationship)).thenReturn(true);
        when(userCredentialRepository.findByUserId(relationship.getUserId())).thenReturn(makeUser());
        when(loveInterestRepository.findLoveInterestById(relationship.getLoveInterestId())).thenReturn(makeLoveInterest());

        Result<Relationship> actual = service.update(relationship);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateMissing() {
        Relationship relationship = makeRelationship();
        relationship.setId(99);

        when(relationshipRepository.update(relationship)).thenReturn(false);
        when(userCredentialRepository.findByUserId(relationship.getUserId())).thenReturn(makeUser());
        when(loveInterestRepository.findLoveInterestById(relationship.getLoveInterestId())).thenReturn(makeLoveInterest());

        Result<Relationship> actual = service.update(relationship);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
    }

    @Test
    void shouldNotUpdateWhenInvalid() {
        Relationship relationship = makeRelationship();
        relationship.setId(3);

        // Should not update when relationship is null
        Result<Relationship> actual = service.update(null);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when id is 0
        relationship.setId(0);
        actual = service.update(relationship);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when start date is null
        relationship = makeRelationship();
        relationship.setStartDate(null);
        actual = service.update(relationship);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when end date is before start date
        relationship = makeRelationship();
        relationship.setStartDate(LocalDate.now());
        relationship.setEndDate(LocalDate.now().minusWeeks(1));
        actual = service.update(relationship);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when relationship status is null
        relationship = makeRelationship();
        relationship.setRelationshipStatus(null);
        actual = service.update(relationship);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when importance level is not between 1 and 5
        relationship = makeRelationship();
        relationship.setImportanceLevel(0);
        actual = service.update(relationship);
        assertEquals(ResultType.INVALID, actual.getType());

        relationship.setImportanceLevel(6);
        actual = service.update(relationship);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when user does not exist
        relationship = makeRelationship();
        relationship.setUserId(99);

        when(userCredentialRepository.findByUserId(relationship.getUserId())).thenReturn(null);
        when(loveInterestRepository.findLoveInterestById(relationship.getLoveInterestId())).thenReturn(makeLoveInterest());

        actual = service.update(relationship);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when love interest does not exist
        relationship = makeRelationship();
        relationship.setLoveInterestId(99);

        when(userCredentialRepository.findByUserId(relationship.getUserId())).thenReturn(makeUser());
        when(loveInterestRepository.findLoveInterestById(relationship.getLoveInterestId())).thenReturn(null);

        actual = service.update(relationship);
        assertEquals(ResultType.INVALID, actual.getType());
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

    private UserCredential makeUser() {
        UserCredential user = new UserCredential();

        user.setId(1);

        return user;
    }

    private LoveInterest makeLoveInterest() {
        LoveInterest loveInterest = new LoveInterest();

        loveInterest.setId(1);

        return loveInterest;
    }
}